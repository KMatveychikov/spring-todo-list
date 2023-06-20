import {IUser} from "../models/IUser";
import {makeAutoObservable} from "mobx";
import AuthService from "../services/AuthService";
import axios, {AxiosResponse} from 'axios'
import {AuthResponse} from "../models/response/AuthResponse";
import {API_URL} from "../http";
import {Role} from "../models/Role";
import TaskService, {Task} from "../services/TaskService";


export default class Store {
    user = {} as IUser;
    isAuth = false;
    isLoading = false;
    token = '';

    tasks = [] as Task[];

    constructor() {
        makeAutoObservable(this);
    }

    setToken(token: string) {
        this.token = token
    }

    setAuth(bool: boolean) {
        this.isAuth = bool;
    }

    setUser(user: IUser) {
        this.user = user;
    }

    setLoading(bool: boolean) {
        this.isLoading = bool;
    }

    async login(email: string, password: string) {
        // if (this.isAuth) await this.logout()
        try {
            const response = await AuthService.login(email, password);
            this.setToken(response.data.token)
            this.setUser(response.data.user);
            this.setAuth(true);

        } catch (e) {

        }
    }

    async registration(name: string, email: string, password: string, role: Role) {
        try {
            const response = await AuthService.registration(name, email, password, role);
            console.log(response)
            this.setToken(response.data.token)
            this.setUser(response.data.user);
            this.setAuth(true);
        } catch (e) {

        }
    }

    async logout() {
        try {
            this.setToken('');
            this.setAuth(false);
            this.setUser({} as IUser);
        } catch (e) {

        }
    }

    async checkAuth() {
        this.setLoading(true);
        try {
            const response = await axios.get<AuthResponse>(`${API_URL}/refresh`, {withCredentials: true})
            console.log(response);
            localStorage.setItem('token', response.data.token);
            this.setAuth(true);
            this.setUser(response.data.user);
        } catch (e) {

        } finally {
            this.setLoading(false);
        }
    }

    async getUsernameById(id: string): Promise<string> {
        const response = await AuthService.getUsernameById(id)
        return response.data
    }


    async getTasks() {
        const response = await TaskService.getAllTasks();
        this.tasks = response.data
    }




}
