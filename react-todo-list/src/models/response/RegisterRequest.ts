import {Role} from "../Role";
import {IUser} from "../IUser";

export interface RegisterRequest {
   name: string
   email: string
   password: string
   role: Role

   token: string
   user: IUser
}