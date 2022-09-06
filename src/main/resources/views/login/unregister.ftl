<#ftl encoding="utf-8">
<#include "/public/header.ftl">
<html lang="fr-FR">
    <body xmlns="http://www.w3.org/1999/html">
        <div id="PageContent">
            <h2>Voulez vous vraiment vous désinscrire?</h2>
            <#if isLogged>
                <#if isAdmin>
                    <p>${username}, sans déconner, t'es admin... Va supprimer ton compte direct en base</p>
                    <#else>
                        <p>En effectuant une désinscription, vous acceptez que toutes vos données stockées soient supprimées et deviennent non récupérables</p>
                        <p>${username}, veuillez confirmer votre volonté de désinscription</p>
                        <form action="/unregister/try" method="POST">
                            <div class="forminput"><input type="submit" value="Oui"></div>
                        </form>
                        <form action="/" method="GET">
                            <div class="forminput"><input type="submit" value="Non"></div>
                        </form>
                </#if>
                <#else>
                    <p>Un utilisateur non connecté ne peux pas se désinscrire</p>
            </#if>
        </div>
    </body>
</html>
<#include "/public/footer.ftl">