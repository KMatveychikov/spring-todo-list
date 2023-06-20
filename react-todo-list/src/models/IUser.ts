import {Role} from "./Role";

export interface IUser {
    _id: string;
    userName: string;
    email: string;
    isActivated: boolean;
    role: Role
}
