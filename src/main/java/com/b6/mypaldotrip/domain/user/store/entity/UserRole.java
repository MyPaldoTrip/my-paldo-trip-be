package com.b6.mypaldotrip.domain.user.store.entity;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum UserRole {
    ROLE_ADMIN("ROLE_ADMIN", "관리자"),
    ROLE_OPERATOR("ROLE_OPERATOR", "운영자"),
    ROLE_USER("ROLE_USER", "유저");

    private final String code;
    private final String value;
}
