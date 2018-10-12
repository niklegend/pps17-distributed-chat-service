export class LoginRequest {

    username: string;

    password: string;

}

export class RegisterRequest {

    username: string;

    firstName: string;

    lastName: string;

    password: string;

    passwordConfirm: string;

}

export class LogoutRequest {

    token: string;

}
