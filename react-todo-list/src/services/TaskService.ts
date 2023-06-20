import {AxiosResponse} from "axios";
import $api from "../http";

export type Task = {
    _id: string
    title: string
    text: string
    todos: Todo[]
    comments: Comment[]
    files: FileInfo[]
    ownerUserId: string
    responsibleUserId: string
    performerUsersId: string[]
    isCompleted: boolean
}

export type Todo = {
    todoId: string
    text: string
    isCompleted: boolean
}

export type Comment = {
    id: string
    authorName: string
    text: string
}

export type FileInfo = {
    fileName: string
    filePath: string
    contentType: string
}

type AddTaskRequest = {
    title: string
    text: string
    responsibleUserId: string
}

export default class TaskService {

    static async addTask(title: string, text: string, responsibleUser: string): Promise<AxiosResponse<AddTaskRequest>> {
        return $api.post<AddTaskRequest>('/task/add_task', {title, text, responsibleUser})
    }

    static async getAllTasks(): Promise<AxiosResponse<Task[]>>{
        return $api.get<Task[]>('/task/all_tasks')
    }


}