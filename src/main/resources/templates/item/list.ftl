<#import "/layout/base.ftl" as layout>
<@layout.page title="Мои вещи">
    <h1>Мои вещи</h1>
    <a href="/items/new" class="btn">Добавить вещь</a>

    <table>
        <tr>
            <th>Название</th>
            <th>Цвет</th>
            <th>Размер</th>
            <th>Сезон</th>
            <th>Цена</th>
            <th>Категория</th>
            <th>Бренд</th>
            <th>Действия</th>
        </tr>
        <#list items as item>
            <tr>
                <td>${item.name!}</td>
                <td>${item.colorName!itemColor!}</td>
                <td>${item.size!}</td>
                <td>${item.season!}</td>
                <td>${item.price!}</td>
                <td>${item.category.name!""}</td>
                <td>${item.brand.name!""}</td>
                <td>
                    <a href="/items/${item.id}/edit" class="btn">Редактировать</a>
                    <form action="/items/${item.id}/delete" method="post" style="display:inline">
                        <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}">
                        <button type="submit" class="btn btn-danger">Удалить</button>
                    </form>
                </td>
            </tr>
        </#list>
    </table>
</@layout.page>