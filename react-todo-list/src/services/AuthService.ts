import $api from "../http";
import {AxiosResponse} from 'axios';
import {AuthResponse} from "../models/response/AuthResponse";
import {RegisterRequest} from "../models/response/RegisterRequest";
import {Role} from "../models/Role";

export default class AuthService {
    static async login(email: string, password: string): Promise<AxiosResponse<AuthResponse>> {
        return $api.post('/auth/login', {email, password})
    }

    static async registration(name: string, email: string, password: string, role: Role): Promise<AxiosResponse<RegisterRequest>> {
        return $api.post<RegisterRequest>('/auth/register', {name, email, password, role})
    }

    static async logout(): Promise<void> {
        return $api.post('/auth/logout')
    }

    static async getUsernameById(id: string): Promise<AxiosResponse<string>> {
        return $api.get('/auth/get_name_by_id/' + id)
    }

}

