<#ftl encoding="utf-8">
<#include "/public/header.ftl">
<html lang="fr-FR">
    <body xmlns="http://www.w3.org/1999/html">
        <form action="/register/try" method="POST">        
            <div id="PageContent">
                <h2>Inscription</h2>
                <div class="forminput"><input type="text" placeholder="Nom d&#39utilisateur" name="name" id="username" required></div>
                <div class="forminput"><input type="text" placeholder="Email" name="email" id="email" required></div>
                <div class="forminput"><input type="password" placeholder="Mot de passe" name="password" id="psw" required></div>                
                <div class="forminput">
                    <button type="submit">S&#39inscrire</button>
                    <input class="boutons" type="reset" value="Annuler">
                </div>
                <p>En créant votre compte vous acceptez de ne pas vous faire spammer d'emails inutiles.</p>
            </div>
            <div id="PageContent">
                <p>Vous avez déjà un compte? <a href="/login">Connexion</a></p>
            </div>
        </form>
    </body>
</html>
<#include "/public/footer.ftl">