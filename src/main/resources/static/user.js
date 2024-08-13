document.addEventListener('DOMContentLoaded', async function () {

    const userService = new UserService()

    const renderService = new RenderService()

    await renderService.renderHeader()
    await renderService.renderUserTable()

});