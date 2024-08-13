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