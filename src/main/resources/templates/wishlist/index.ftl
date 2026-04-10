<#import "/layout/base.ftl" as layout>
<@layout.page title="Вишлист">
    <h1>Вишлист</h1>

    <div id="wishlist-form">
        <h3>Добавить желание</h3>
        <input type="text" id="wish-name" placeholder="Название">
        <input type="number" step="0.01" id="wish-price" placeholder="Примерная цена">
        <input type="text" id="wish-url" placeholder="Ссылка на товар">
        <input type="text" id="wish-note" placeholder="Заметка">
        <button onclick="addWish()" class="btn">Добавить</button>
    </div>

    <h3>Курсы валют</h3>
    <#list rates?keys as key>
        ${key} = ${rates[key]}
    </#list>

    <table>
        <thead>
        <tr>
            <th>Название</th>
            <th>Цена</th>
            <th>Ссылка</th>
            <th>Заметка</th>
            <th>Действия</th>
        </tr>
        </thead>
        <tbody id="wishlist-body">
        </tbody>
    </table>

    <input type="hidden" id="csrf-token" value="${_csrf.token}">
    <input type="hidden" id="csrf-header" value="${_csrf.headerName}">
</@layout.page>