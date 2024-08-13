document.addEventListener('DOMContentLoaded', async function () {

    const userService = new UserService()

    const renderService = new RenderService()
    await renderService.renderUsersTable()
    await renderService.renderRoles()
    await renderService.renderHeader()

    const eventsService = new EventsService(userService)
    await eventsService.bindFormDataUser()
    await eventsService.bindModal()
});







