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
let fullname = null;
let selectedRoomId = null;
let chatRoomId = null;
let senderId = null;
let chatRoomName = null;
document.getElementById('usernameForm').addEventListener('submit', function(event) {
    event.preventDefault();

    var email = document.getElementById('nickname').value;
    var password = document.getElementById('fullname').value;

    var xhr = new XMLHttpRequest();
    xhr.open('POST', 'http://localhost:8080/api/v1/users/login', true);
    xhr.setRequestHeader('Content-Type', 'application/json');
    xhr.onreadystatechange = function () {
        if (xhr.readyState === 4 && xhr.status === 200) {
            var token = xhr.getResponseHeader('Authorization');
            console.log("토큰 : " + token);

            // 'Bearer '를 제거하고 토큰만을 저장합니다.
            localStorage.setItem('token', token.split(' ')[1]);

            connect(event, email, password);
        }
    };
    var data = JSON.stringify({"email": email, "password": password});
    xhr.send(data);
});



function connect(event, email, password) {
    event.preventDefault();

    nickname = email.trim();
    fullname = password.trim();

    if (nickname && fullname) {
        usernamePage.classList.add('hidden');
        chatPage.classList.remove('hidden');

        const socket = new SockJS('/ws');
        stompClient = Stomp.over(socket);

        stompClient.connect({}, onConnected, onError);
    }


}

// function connect(event) {
//     nickname = document.querySelector('#nickname').value.trim();
//     fullname = document.querySelector('#fullname').value.trim();
//
//
//     if (nickname && fullname) {
//         usernamePage.classList.add('hidden');
//         chatPage.classList.remove('hidden');
//
//         const socket = new SockJS('/ws');
//         stompClient = Stomp.over(socket);
//
//         stompClient.connect({}, onConnected, onError);
//     }
//     event.preventDefault();
// }

function onConnected() {

    stompClient.subscribe(`/topic/public/`, onMessageReceived);
    stompClient.subscribe(`/user/public`, onMessageReceived);
    stompClient.subscribe(`/topic/public/${chatRoomId}`, onMessageReceived);

    // register the connected user
    stompClient.send("/app/user.addUser",
        {},
        JSON.stringify({nickName: nickname, fullName: fullname, status: 'ONLINE'})
    );
    document.querySelector('#connected-user-fullname').textContent = nickname;
    findAndDisplayConnectedRooms().then();
    console.log("토큰"+localStorage.getItem('token'));
    fetch('http://localhost:8080/api/v1/users/getRole', {
        headers: {
            'Authorization': 'Bearer ' + localStorage.getItem('token') // 토큰을 헤더에 포함
        }
    })
    .then(response => response.json()) // response.json()을 호출하여 반환된 Promise를 다음 then으로 전달합니다.
    .then(data => {
        // 이 콜백은 response.json() Promise가 이행될 때 호출됩니다.
        // 따라서 data는 response.json()의 결과입니다.
        console.log("data: ", data);

        if (data && data.role === 'ROLE_ADMIN') {
            // 사용자의 역할이 ROLE_ADMIN이면, 'hidden' 클래스를 제거합니다.
            document.getElementById('adminChatRoom').classList.remove('hidden');
        }
    })
    .catch(error => console.error('Error:', error));


}

async function findAndDisplayConnectedRooms() {
    const connectedRoomResponse = await fetch('/api/v1/rooms');
    console.log(connectedRoomResponse);
    let connectedRooms = await connectedRoomResponse.json();
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
    const connectedRoomResponse = await fetch('/api/v1/rooms');
    let connectedRooms = await connectedRoomResponse.json();

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
    const message = document.createElement('p');
    message.textContent = content;
    messageContainer.appendChild(message);
    chatArea.appendChild(messageContainer);
}

async function fetchAndDisplayUserChat() {
    chatRoomId = selectedRoomId;
    const userChatResponse = await fetch(`/api/v1/messages/${selectedRoomId}`);
    const userChat = await userChatResponse.json();
    chatArea.innerHTML = '';
    userChat.forEach(chat => {
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
        displayMessage(nickname, messageInput.value.trim());
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
        document.querySelector(`#id-${selectedRoomId}`).classList.add('active');
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
        JSON.stringify({nickName: nickname, fullName: fullname, status: 'OFFLINE'})
    );
    window.location.reload();
}

document.getElementById('createRoomButton').addEventListener('click', function(event) {
    const createText = document.getElementById('createText').value;
    stompClient.send("/app/chat/rooms", {}, JSON.stringify({chatRoomName: createText }));
})
//stompClient.send("/app/chat/rooms", {}, JSON.stringify(createRoomRequest));
//생성 버튼 누르면 작동함
document.getElementById('deleteButton').addEventListener('click', function(event) { // 'deleteButton'으로 수정
    const chatRoomName = document.getElementById('deleteRoom').value;

    fetch('/api/v1/chat/' + chatRoomName, {
        method: 'DELETE'
    })
    .then(response => response.json())
    .then(data => console.log(data))
    .catch(error => console.error('Error:', error));
});
document.getElementById('updateButton').addEventListener('click', function(event) {
    const originalRoomName = document.getElementById('updateText1').value;
    const newRoomName = document.getElementById('updateText2').value;

    fetch(`/api/v1/chat/chatRoomName/${originalRoomName}/updateRoomName/${newRoomName}`, {
        method: 'PUT'
    })
    .then(response => response.json())
    .then(data => console.log(data))
    .catch(error => console.error('Error:', error));
});



usernameForm.addEventListener('submit', connect, true); // step 1
messageForm.addEventListener('submit', sendMessage, true);
logout.addEventListener('click', onLogout, true);
window.onbeforeunload = () => onLogout();




