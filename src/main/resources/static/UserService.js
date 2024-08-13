class UserService {

    async getUsers() {
        const response = await fetch("/admin/obtainAll");
        return await response.json();
    }

    async getUserById(id) {
        const response = await fetch("/admin/" + id);
        return await response.json();
    }

    async getCurrentUser() {
        const response = await fetch("/user/current");
        return await response.json();
    }

    async getRoles() {
        const response = await fetch("/admin/roles");
        return await response.json();
    }

    async saveUser(data) {
        await fetch("/admin/save", {
            body: JSON.stringify(data),
            headers: {'Content-Type': 'application/json'},
            method: 'post'
        })
    }

    async deleteUserById(id) {
        await fetch("/admin/delete/" + id)
    }
}


