document.addEventListener('DOMContentLoaded', async function () {

    const userService = new UserService()

    const renderService = new RenderService()
    await renderService.renderUser()
    await renderService.renderRoles()

    const eventsService = new EventsService(userService)
    await eventsService.bindFormDataUser()
    await eventsService.bindModal()
});

class UserService {

    async getUsers() {
        const response = await fetch("/api/users");
        return await response.json();
    }

    async getUserById(id) {
        const response = await fetch("/api/user/" + id);
        return await response.json();
    }

    async getRoles() {
        const response = await fetch("/api/roles");
        return await response.json();
    }

    async saveUser(data) {
        await fetch("/api/save", {
            body: JSON.stringify(data),
            headers: {'Content-Type': 'application/json'},
            method: 'post'
        })
    }

    async deleteUserById(id) {
        await fetch("/api/delete/" + id)
    }
}

class RenderService {
    userService;
    userTable;
    userNewRolesSelect;

    constructor() {
        this.userService = new UserService()
        this.userTable = document.querySelector(".render-user-body");
        this.userNewRolesSelect = document.querySelector(".render-new-user-roles");
    }

    async renderUser() {
        if (this.userTable == null) {
            return
        }
        this.userTable.innerHTML = ""

        const userList = await this.userService.getUsers()
        userList.forEach(user => {
            this.userTable.innerHTML += `
<tr>
    <th>${user.id}</th>
    <th>${user.name}</th>
    <th>${user.email}</th>
    <th>${user.roles.map(role => role.name).join(' ')}</th>
    <td>
     <button type="button" class="btn btn-info btn-small js-user-edit-button" data-bs-toggle="modal" data-bs-target="#modalUserData" data-action="edit" data-id="${user.id}">
        Edit
    </button>
</td>
    <td>
    <button type="button" class="btn btn-danger btn-small js-user-delete-button" data-bs-toggle="modal" data-bs-target="#modalUserData" data-action="delete" data-id="${user.id}">
        Delete
    </button>
    </td>
<tr
`
        })
    }

    async renderRoles() {
        if (this.userNewRolesSelect == null) {
            return
        }
        const roleList = await this.userService.getRoles()
        this.userNewRolesSelect.innerHTML = ""
        roleList.forEach(role => {
            this.userNewRolesSelect.innerHTML += `
                <option value="${role.id}">${role.name}</option>
            `
        })
    }
}

class EventsService {
    createUserForm;
    dataUserModal;
    dataUserForm;
    userService;
    myModal;

    constructor(userService) {
        this.userService = userService;
        this.createUserForm = document.querySelector('.js-user-create-form')
        this.dataUserForm = document.querySelector('.js-user-data-form')
        this.dataUserModal = document.querySelector('#modalUserData')
        this.myModal = new bootstrap.Modal('#modalUserData')
    }

    async bindFormDataUser() {
        this.createUserForm.addEventListener("submit", async event => {
            event.preventDefault()
            let formData = new FormData(event.target);

            await Helpers.saveUser(this.userService, formData)
            this.createUserForm.reset()
            await Helpers.switchToUserList()
        })

        this.dataUserForm.addEventListener("submit", async event => {
            event.preventDefault()
            let formData = new FormData(event.target);
            if (event.target.method === 'post') {
                await Helpers.saveUser(this.userService, formData)
            } else {
                await this.userService.deleteUserById(formData.get('id'))
            }
            this.dataUserForm.reset()
            this.myModal.hide()
            await (new RenderService()).renderUser()
        })
    }

    async bindModal() {
        const roleList = await this.userService.getRoles()

        this.dataUserModal.addEventListener("show.bs.modal", async (event) => {
            const userId = event.relatedTarget.dataset.id
            const action = event.relatedTarget.dataset.action
            const userData = await this.userService.getUserById(userId)

            const form = event.target.querySelector('#formUserData')
            const idField = form.querySelector('#modal-hidden-id-field')
            const idVisibleField = form.querySelector('#modal-id-field')

            const nameField = form.querySelector('#modal-first-name-field')
            const emailField = form.querySelector('#modal-email-field')
            const passwordField = form.querySelector('#modal-password-field')
            const rolesField = form.querySelector('#modal-roles-field')

            const buttonEdit = event.target.querySelector('#button-edit')
            const buttonDelete = event.target.querySelector('#button-delete')

            // action
            if (action === 'edit') {
                nameField.disabled = false
                emailField.disabled = false
                passwordField.disabled = false
                rolesField.disabled = false
                buttonEdit.hidden = false
                buttonDelete.hidden = true
                form.method = 'post'
            } else {
                nameField.disabled = true
                emailField.disabled = true
                passwordField.disabled = true
                rolesField.disabled = true
                buttonEdit.hidden = true
                buttonDelete.hidden = false
                form.method = 'delete'
            }

            idField.value = userData.id
            idVisibleField.value = userData.id
            nameField.value = userData.name
            emailField.value = userData.email
            passwordField.value = ''

            rolesField.innerHTML = ""
            roleList.forEach(role => {
                rolesField.innerHTML += `
                <option value="${role.id}">${role.name}</option>
            `
            })
        })
    }
}

class Helpers {
    static async switchToUserList() {
        document.querySelector('#nav-table-tab').click()
        await (new RenderService()).renderUser()
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