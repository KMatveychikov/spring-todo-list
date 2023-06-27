import $api from "../http";

export type Board = {
    _id: string,
    title: string,
    tasksId: string[]
}

export default class BoardService {
    static async addBoard(title: string) {
        return $api.post("/board/add", null, {
            params: title
        })
    }

    static async getBoards() {
        return $api.get("/api/v1/board/get_boards")
    }

    static async getTasksByBoard(boardId: string) {
        return $api.get("/board/" + boardId + "/tasks")
    }
}