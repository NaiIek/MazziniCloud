<#ftl encoding="utf-8">
<#include "/public/header.ftl">
<html lang="fr-FR">
    <body xmlns="http://www.w3.org/1999/html">
        <#if isLogged>
            <#if isAdmin>
                <div id="PageContent">
                    <h2>Stockage Utilisateur</h2>
                    <#list files as file>
                        <div id="FileContent">
                            <p>Fichier: ${file.getFileName()}</p>
                            <#assign rnm_link = "/rnmfile/${file.id}">
                            <form action="${rnm_link}" method="POST">
                                <div class="forminput"><input type="text" placeholder="Nouveau nom" name="rename" required></div>
                                <div class="forminput"><input type="submit" value="Renommer"></div>
                            </form>
                            <#assign dwl_link = "/dwlfile/${file.id}">
                            <form action="${dwl_link}" method="GET">
                                <div class="forminput"><input type="submit" value="Télécharger"></div>
                            </form>
                            <#assign del_link = "/delfile/${file.id}">
                            <form action="${del_link}" method="POST">
                                <div class="forminput"><input type="submit" value="Supprimer"></div>
                            </form>
                        </div>
                    </#list>
                </div>
            <#else>
                <div id="PageContent">
                    <h2>Mec dégage, t'as rien à voir ici</h2>
                    <p>Ta présence sur cette page à été signalée et tu risques un banissement en cas de récidive</p>
                </div>
            </#if>
        <#else>
            <div id="PageContent">
                <h2>Bienvenue sur le Mazzland Cloud Service</h2>
                <p>Une fois connectés, vous retrouverez ici la possibilité de stocker et consulter vos fichiers.</p>
            </div> 
        </#if>     
    </body>
</html>
<#include "/public/footer.ftl">