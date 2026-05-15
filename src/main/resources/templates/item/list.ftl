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
            <th>Рекомендация</th>
        </tr>
        <#list items as item>
            <tr>
                <td>${item.name!}</td>
                <td>${colorNames[item.id?c]!item.color!}</td>
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
                <td>
                    <#assign matches = matchingItems[item.id?c]![]>
                    <#if matches?size gt 0>
                        Подходит к:
                        <#list matches as match>
                            ${match.name}<#if match_has_next>, </#if>
                        </#list>
                    <#else>
                        Нет подходящих вещей
                    </#if>
                </td>
            </tr>
        </#list>
    </table>
</@layout.page>