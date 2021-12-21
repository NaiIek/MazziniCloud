<#ftl encoding="utf-8">
<#include "/public/header.ftl">
<html lang="fr-FR">
    <body xmlns="http://www.w3.org/1999/html">
        <#if isLogged>
            <div id="PageContent">
                <h2>Ajouter des fichiers</h2>
                <form action="/addfile" method="POST" enctype="multipart/form-data">
                    <div class="forminput"><input type="file" name="uploaded_file" id="uploaded_file" accept=".zip,.c,.py,.java,.txt,.pdf,.doc,.docx,.xls,.xlsx,image/*,video/*,audio/*" required></div>                
                    <div class="forminput">
                        <button type="submit">Uploader</button>
                        <input class="boutons" type="reset" value="Annuler">
                    </div>
                </form>
                <p>Les fichiers que vous stockez sur le site ne seront visible que par vous et les administrateurs, évitez de stocker des fichiers sensibles.</p>
            </div>
            <div id="PageContent">
                <h2>Mes Fichiers</h2>
                <#list files as file>
                    <div id="FileContent">
                        <p>Fichier: ${file.getFileName()}</p>
                        <#assign del_link = "/delfile/${file.id}">
                        <form action="${del_link}" method="POST">
                            <input type="submit" value="Supprimer">
                        </form>
                        <#assign dwl_link = "/dwlfile/${file.id}">
                        <form action="${dwl_link}" method="GET">
                            <input type="submit" value="Télécharger">
                        </form>
                    </div>
                </#list>
                <p>feature à implémenter (renommer fichier)</p>
            </div>
            <#else>
                <div id="PageContent">
                    <h2>Bienvenue sur le Mazzini Cloud</h2>
                    <p>Une fois connectés, vous retrouverez ici la possibilité de stocker et consulter vos fichiers.</p>
                </div>
        </#if>
    </body>
</html>
<#include "/public/footer.ftl">