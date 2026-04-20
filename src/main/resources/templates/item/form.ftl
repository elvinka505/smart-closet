<#import "/layout/base.ftl" as layout>
<@layout.page title="Вещь">
    <h1><#if item.id??>Редактировать<#else>Добавить</#if> вещь</h1>

    <form action="/items" method="post">
        <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}">
        <#if item.id??>
            <input type="hidden" name="id" value="${item.id}">
        </#if>

        <div>
            <label>Название:</label>
            <input type="text" name="name" value="${item.name!}" required>
        </div>
        <div>
            <label>Размер:</label>
            <input type="text" name="size" value="${item.size!}">
        </div>
        <div>
            <label>Сезон:</label>
            <input type="text" name="season" value="${item.season!}">
        </div>
        <div>
            <label>Цена:</label>
            <input type="number" step="0.01" name="price" value="${item.price!}">
        </div>
        <div>
            <label>Категория:</label>
            <select name="category">
                <option value="">-- выбрать --</option>
                <#list categories as cat>
                    <option value="${cat.id}"<#if item.category?? && item.category.id == cat.id> selected</#if>>${cat.name}</option>
                </#list>
            </select>
        </div>
        <div>
            <label>Бренд:</label>
            <select name="brand">
                <option value="">-- выбрать --</option>
                <#list brands as b>
                    <option value="${b.id}"<#if item.brand?? && item.brand.id == b.id> selected</#if>>${b.name}</option>
                </#list>
            </select>
        </div>
        <div>
            <label>Цвет:</label>
            <input type="color" name="color" value="${item.color!'#FFFFFF'}">
        </div>

        <button type="submit">Сохранить</button>
    </form>
    <a href="/items">Назад к списку</a>
</@layout.page>