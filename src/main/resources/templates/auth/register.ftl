<#import "/layout/base.ftl" as layout>
<@layout.page title="Регистрация">
    <h1>Регистрация</h1>
    <form action="/register" method="post">
        <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}">
        <div>
            <label>Имя пользователя:</label>
            <input type="text" name="username" required>
        </div>
        <div>
            <label>Email:</label>
            <input type="email" name="email" required>
        </div>
        <div>
            <label>Пароль:</label>
            <input type="password" name="password" required>
        </div>
        <button type="submit">Зарегистрироваться</button>
    </form>
    <p><a href="/login">Уже есть аккаунт? Войти</a></p>
</@layout.page>