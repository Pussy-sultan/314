class RenderService {
    userService;
    userTable;
    userNewRolesSelect;
    userHeader;

    constructor() {
        this.userService = new UserService()
        this.userTable = document.querySelector(".render-user-body");
        this.userNewRolesSelect = document.querySelector(".render-new-user-roles");
        this.userHeader = document.querySelector(".render-header-user");
    }

    async renderUsersTable() {
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

    async renderHeader() {
        if (this.userHeader == null) {
            return
        }
        const user = await this.userService.getCurrentUser()
        this.userHeader.innerHTML = `
        <strong class="wf-bold">${user.email}</strong>
                        with roles:
                        <span class="render-role-list-user">${user.roles.map(role => role.name).join(' ')}</span>
        `

    }

    async renderUserTable() {
        if (this.userTable == null) {
            return
        }
        const user = await this.userService.getCurrentUser()
        this.userTable.innerHTML = `
        <tr>
            <th scope="row" >${user.id}</th>
            <td>${user.name}</td>
            <td>${user.email}</td>
            <td>${user.roles.map(role => role.name).join(' ')}</td>
        </tr>
        `
    }
}