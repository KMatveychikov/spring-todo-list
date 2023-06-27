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
    deadline: Date
    tags: Tag[]
    isCompleted: boolean
}
export type Tag = {
    color: string;
    text: string;

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
    boardId: string
    deadline: Date
}

export default class TaskService {

    static async addTask(title: string, text: string, responsibleUser: string, deadline: Date, boardId: string): Promise<AxiosResponse<AddTaskRequest>> {
        return $api.post<AddTaskRequest>('/board/add_task', {title, text, responsibleUser, deadline, boardId})
    }

    static async addComment(taskId: string, text: string) {
        return $api.post("/" + taskId + "/add_comment", null, {params: text})

    }

    static async editTaskText(taskId: string, text: string) {
        return $api.put("/" + taskId + "/edit_text", null, {params: text})
    }

    static async addTag(taskId: string, title: string, color: string) {
        return $api.post("/" + taskId + "/add_tag", null, {
            params: {
                title,
                color
            }
        })

    }

    static async deleteTag(taskId: string, tagId: string) {
        return $api.delete("/" + taskId + "/delete_tag", {params: tagId})
    }

    static async addTodo(taskId: string, todoText: string) {
        return $api.post("/" + taskId + "/add_todo", null, {params: todoText})
    }

    static async completeTodo(taskId: string, todoId: string) {
        return $api.post("/" + taskId + "/complete_todo", null, {params: todoId})
    }

    static async completeTask(taskId: string) {
        return $api.post("/" + taskId + "/complete_task")
    }

    static async addPerformerUser(taskId: string, userId: string) {
        return $api.post("/" + taskId + "/add_performer", null, {params: userId})
    }

    static async removePerformerUser(taskId: string, userId: string) {
        return $api.delete("/" + taskId + "/remove_performer", {params: userId})
    }

}