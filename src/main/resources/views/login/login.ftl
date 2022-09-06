<#ftl encoding="utf-8">
<#include "/public/header.ftl">
<html lang="fr-FR">
    <body xmlns="http://www.w3.org/1999/html">
        <div id="PageContent">
            <h2>Connexion</h2>
            <form action="/login/try" method="POST">
                <div class="forminput"><input type="text" placeholder="Nom d&#39utilisateur" name="name" id="username" required></div>
                <div class="forminput"><input type="password" placeholder="Mot de passe" name="password" id="psw" required></div>
                <div class="forminput">
                    <button type="submit">Connexion</button>
                    <input class="boutons" type="reset" value="Annuler">
                </div>
            </form>
        </div>
        <div id="PageContent">
            <p>Vous n'avez pas encore de compte? <a href="/register">S'inscrire</a></p>
        </div>
    </body>
</html>
<#include "/public/footer.ftl">