class Helpers {
    static async switchToUserList() {
        document.querySelector('#nav-table-tab').click()
        await (new RenderService()).renderUsersTable()
    }

    static async saveUser(service, formData) {
        let roles = []
        const allRole = await Helpers.getRoles()
        formData.getAll('roles').forEach(role => {
            roles.push(allRole[role])
        })
        const userData = {
            id: formData.get('id'),
            name: formData.get('name'),
            email: formData.get('email'),
            password: formData.get('password'),
            roles: roles
        }
        await service.saveUser(userData)
    }

    static async getRoles() {
        return {
            1: "ROLE_ADMIN",
            2: "ROLE_USER"
        }
    }
}