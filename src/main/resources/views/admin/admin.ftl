<#ftl encoding="utf-8">
<#include "/public/header.ftl">
<html lang="fr-FR">
    <body xmlns="http://www.w3.org/1999/html">
        <#if isAdmin>
            <#list users as user>
                <div id="PageContent">
                    <h2><span class="user_id">#${user.id}</span> ${user.name}</h2>
                    <p>Mail: ${user.email}</p>
                    <p>MDP : ${user.pwd}</p>
                    <p>Admin : ${user.isAdmin}</p>
                    <p>Banned : ${user.isBanned}</p>
                    <#assign ban_link = "/ban/${user.id}">
                    <#assign unban_link = "/unban/${user.id}">
                    <#assign view_files_link = "/admin/viewfiles/${user.id}">
                    <#if user.isBanned != 1>
                        <form action="${ban_link}" method="POST">
                            <input type="submit" value="Ban">
                        </form>
                    <#else>
                        <form action="${unban_link}" method="POST">
                            <input type="submit" value="Unban">
                        </form>
                    </#if>
                    <form action="${view_files_link}" method="GET">
                        <input type="submit" value="View Files">
                    </form>                  
                </div>
            </#list> 
        </#if>     
    </body>
</html>
<#include "/public/footer.ftl">