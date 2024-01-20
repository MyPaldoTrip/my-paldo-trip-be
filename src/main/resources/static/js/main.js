'use strict';

const usernamePage = document.querySelector('#username-page');
const chatPage = document.querySelector('#chat-page');
const usernameForm = document.querySelector('#usernameForm');
const messageForm = document.querySelector('#messageForm');
const messageInput = document.querySelector('#message');
const connectingElement = document.querySelector('.connecting');
const chatArea = document.querySelector('#chat-messages');
const logout = document.querySelector('#logout');
const createText = document.querySelector('#createText');

let stompClient = null;
let nickname = null;
let password = null;
let selectedRoomId = null;
let chatRoomId = null;
let senderId = null;
let chatRoomName = null;
let userRole = null;
let username = null;






function connect() {
    fetch('/api/v1/chat-rooms/users/getRole', {
        headers: {
            'Content-Type': 'application/json',
            'Authorization': 'Bearer ' + localStorage.getItem('token') // 토큰을 헤더에 포함
        }
    })
    .then(response => response.json()) // response.json()을 호출하여 반환된 Promise를 다음 then으로 전달합니다.
    .then(data => {
        userRole = data.data.role;
        nickname = data.data.name;
        console.log("유저 data: ", data);

    })
    .catch(error => console.error('Error:', error));

        const socket = new SockJS('/ws');
        stompClient = Stomp.over(socket);

        stompClient.connect({}, onConnected, onError);

}


function onConnected() {
    stompClient.subscribe(`/topic/public/`, onMessageReceived);
    stompClient.subscribe(`/user/public`, onMessageReceived);
    stompClient.subscribe(`/topic/public/${chatRoomId}`, onMessageReceived);

    // register the connected user
    stompClient.send("/app/user.addUser",
        {},
        JSON.stringify({nickName: nickname, password: password, status: 'ONLINE'})
    );
    document.querySelector('#connected-user-fullname').textContent = nickname;
    findAndDisplayConnectedRooms().then();
    console.log("토큰"+localStorage.getItem('token'));
    fetch('/api/v1/chat-rooms/users/getRole', {
        headers: {
            'Content-Type': 'application/json',
            'Authorization': 'Bearer ' + localStorage.getItem('token') // 토큰을 헤더에 포함
        }
    })
    .then(response => response.json()) // response.json()을 호출하여 반환된 Promise를 다음 then으로 전달합니다.
    .then(data => {
        // 이 콜백은 response.json() Promise가 이행될 때 호출됩니다.
        // 따라서 data는 response.json()의 결과입니다.
        console.log("data: ", data);

        if (data && data.data.role === 'ROLE_ADMIN') { // data.data.role을 사용하여 역할 확인
            // 사용자의 역할이 ROLE_ADMIN이면, 'hidden' 클래스를 제거합니다.
            document.getElementById('adminChatRoom').classList.remove('hidden');
        }
    })
    .catch(error => console.error('Error:', error));
}


async function findAndDisplayConnectedRooms() {
    const connectedRoomResponse = await fetch('/api/v1/chat-rooms/rooms', {
        headers: {
            'Content-Type': 'application/json',
            'Authorization': 'Bearer ' + localStorage.getItem('token') // 토큰을 헤더에 포함
        }
    });
    console.log(connectedRoomResponse);
    let connectedRooms = await connectedRoomResponse.json();
    connectedRooms = connectedRooms.data; // data 필드를 추출하여 connectedRooms에 재할당
    console.log(connectedRooms);
    //connectedRooms = connectedRooms.filter(room => room.nickName !== nickname);
    const connectedUsersList = document.getElementById('connectedUsers');
    connectedUsersList.innerHTML = '';

    connectedRooms.forEach(room => {
        appendUserElement(room, connectedUsersList);
        if (connectedRooms.indexOf(room) < connectedRooms.length - 1) {
            const separator = document.createElement('li');
            separator.classList.add('separator');
            connectedUsersList.appendChild(separator);
        }
    });
}


function appendUserElement(room, connectedUsersList) {
    const listItem = document.createElement('li');
    listItem.classList.add('user-item');
    listItem.id = room.chatRoomId;
    console.log(listItem.id);

    const userImage = document.createElement('img');
    userImage.src = '/js/user_icon.png';
    userImage.alt = room.chatRoomName;

    const usernameSpan = document.createElement('span');
    usernameSpan.textContent = room.chatRoomName;

    const receivedMsgs = document.createElement('span');
    receivedMsgs.textContent = '0';
    receivedMsgs.classList.add('nbr-msg', 'hidden');

    listItem.appendChild(userImage);
    listItem.appendChild(usernameSpan);
    listItem.appendChild(receivedMsgs);

    listItem.addEventListener('click', userItemClick);

    connectedUsersList.appendChild(listItem);
}

async function userItemClick(event) {
    document.querySelectorAll('.user-item').forEach(item => {
        item.classList.remove('active');
    });
    messageForm.classList.remove('hidden');

    const clickedUser = event.currentTarget;
    clickedUser.classList.add('active');

    selectedRoomId = clickedUser.getAttribute('id');
    chatRoomName = await findRoomName(selectedRoomId);
    console.log(chatRoomName);


    fetchAndDisplayUserChat().then();

    const nbrMsg = clickedUser.querySelector('.nbr-msg');
    nbrMsg.classList.add('hidden');
    nbrMsg.textContent = '0';
}

async function findRoomName(selectedRoomId){
    const connectedRoomResponse = await fetch('/api/v1/chat-rooms/rooms', {
        headers: {
            'Content-Type': 'application/json',
            'Authorization': 'Bearer ' + localStorage.getItem('token') // 토큰을 헤더에 포함
        }
    });
    let connectedRooms = await connectedRoomResponse.json();
    connectedRooms = connectedRooms.data; // data 필드를 추출하여 connectedRooms에 재할당

    for(let room of connectedRooms){
        if(room.chatRoomId === selectedRoomId){
            return room.chatRoomName;
        }
    }

    return null; // 해당하는 방을 찾지 못한 경우, null 반환
}

function displayMessage(senderId, content) {
    const messageContainer = document.createElement('div');
    messageContainer.classList.add('message');
    if (senderId === nickname) {
        messageContainer.classList.add('sender');
    } else {
        messageContainer.classList.add('receiver');
    }
    const username2 = document.createElement('p');
    const message = document.createElement('p');
    username2.textContent = senderId;
    message.textContent = content;
    messageContainer.appendChild(username2);
    messageContainer.appendChild(message);
    chatArea.appendChild(messageContainer);
}

async function fetchAndDisplayUserChat() {
    chatRoomId = selectedRoomId;
    const userChatResponse = await fetch(`/api/v1/chat-rooms/${chatRoomId}`, {
        headers: {
            'Content-Type': 'application/json',
            'Authorization': 'Bearer ' + localStorage.getItem('token') // 토큰을 헤더에 포함
        }
    });

    const userChat = await userChatResponse.json();
    //const chatMessageSenders = userChat.data.chatMessageSenders;   // chatMessageSenders 값을 추출
    const chatMessages = userChat.data.chatMessages;   // chatMessages 값을 추출
    chatArea.innerHTML = '';
    // chatMessageSenders.forEach(chat => {
    //     displayMessage(chat.senderId, chat.content);
    // });
    chatMessages.forEach(chat => {
        displayMessage(chat.senderId, chat.content);
    });
    chatArea.scrollTop = chatArea.scrollHeight;
}




function onError() {
    connectingElement.textContent = 'Could not connect to WebSocket server. Please refresh this page to try again!';
    connectingElement.style.color = 'red';
}


function sendMessage(event) {
    const messageContent = messageInput.value.trim();
    console.log("messageContent = "+messageContent);
    if (messageContent && stompClient) {
        const chatMessageEntity = {
            chatRoomId : chatRoomId,
            chatRoomName: chatRoomName,
            senderId: nickname,
            recipientId: selectedRoomId,
            content: messageInput.value.trim(),
            timestamp: new Date()
        };
        // chatRoomId와 senderId를 실제 값으로 대체해야 합니다.

        stompClient.send(`/app/chatting/${chatRoomId}`, {}, JSON.stringify(chatMessageEntity));
        //stompClient.send("/app/chat", {}, JSON.stringify(chatMessageEntity));
        displayMessage(senderId, messageInput.value.trim());
        messageInput.value = '';
    }
    chatArea.scrollTop = chatArea.scrollHeight;
    event.preventDefault();
}



async function onMessageReceived(payload) {
    await findAndDisplayConnectedRooms();
    console.log('Message received', payload);
    const message = JSON.parse(payload.body);
    if (selectedRoomId && selectedRoomId === message.chatRoomId) {
        displayMessage(message.senderId, message.content);
        chatArea.scrollTop = chatArea.scrollHeight;
        await fetchAndDisplayUserChat(); // 채팅 메시지를 가져와서 화면을 갱신
    }

    if (selectedRoomId) {
        document.querySelector(`#${selectedRoomId}`).classList.add('active');
        //선택된 방이 highlight 됨
        //현재 몽고DB에 roomId들이 숫자+문자로 저장이 되어있는데 CSS에서는 숫자로 시작하는 id값 할당이 되지 않음
        //예시 - 871d5281-edc8-44cf-a85d-7c46db2c69bd
    } else {
        messageForm.classList.add('hidden');
    }


    const notifiedUser = document.querySelector(`#${message.senderId}`);
    if (notifiedUser && !notifiedUser.classList.contains('active')) {
        const nbrMsg = notifiedUser.querySelector('.nbr-msg');
        nbrMsg.classList.remove('hidden');
        nbrMsg.textContent = '';
    }
}

function onLogout() {
    stompClient.send("/app/user.disconnectUser",
        {},
        JSON.stringify({nickName: nickname, password: password, status: 'OFFLINE'})
    );
    window.location.reload();
}

// 방 이름을 입력하고 방을 생성하는 이벤트 핸들러
document.getElementById('createRoomButton').addEventListener('click', function(event) {
    // 방 이름 가져오기
    const createText = document.getElementById('createText').value;
    const roomData = { chatRoomName: createText };

    // 로컬 스토리지에서 토큰 가져오기
    const token = localStorage.getItem('token');

    // 방 생성 요청 보내기
    fetch('/api/v1/chat-rooms/rooms', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json',
            'Authorization': 'Bearer ' + token // 로컬 스토리지의 토큰 사용
        },
        body: JSON.stringify(roomData)
    })
    .then(response => {
        if (!response.ok) {
            throw new Error("HTTP error " + response.status);
        }
        return response.json();
    })
    .then(data => console.log(data)) // 채팅방 생성 결과를 콘솔에 출력
    .catch((error) => console.error('Error:', error));
});


//stompClient.send("/app/chat/rooms", {}, JSON.stringify(createRoomRequest));
//생성 버튼 누르면 작동함
document.getElementById('deleteButton').addEventListener('click', function(event) { // 'deleteButton'으로 수정
    const chatRoomName = document.getElementById('deleteRoom').value;

    fetch('/api/v1/chat-rooms/rooms/' + chatRoomName, {
        method: 'DELETE',
        headers: {
            'Content-Type': 'application/json',
            'Authorization': 'Bearer ' + localStorage.getItem('token')// 인증 토큰 포함
        }
    })
    .then(response => response.json())
    .then(data => console.log(data))
    .catch(error => console.error('Error:', error));
});
document.getElementById('updateButton').addEventListener('click', function(event) {
    const originalRoomName = document.getElementById('updateText1').value;
    const newRoomName = document.getElementById('updateText2').value;
    const token = localStorage.getItem('token'); // 로컬 스토리지에서 토큰을 가져옵니다.

    fetch(`/api/v1/chat-rooms/chatRooms/${originalRoomName}`, {
        method: 'PUT',
        headers: {
            'Content-Type': 'application/json',
            'Authorization': 'Bearer ' +localStorage.getItem('token') // 인증 토큰 포함
        },
        body: JSON.stringify({ newChatRoomName: newRoomName }) // 변경할 채팅방 이름을 JSON 형식으로 전달합니다.
    })
    .then(response => response.json())
    .then(data => console.log(data))
    .catch(error => console.error('Error:', error));
});




// usernameForm.addEventListener('submit', connect, true); // step 1
messageForm.addEventListener('submit', sendMessage, true);
logout.addEventListener('click', onLogout, true);
window.onbeforeunload = () => onLogout();
connect();




