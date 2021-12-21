<#ftl encoding="utf-8">
<meta charset="UTF-8">
<html lang="fr-FR">
    <head>
        <meta name="viewport" content="width=device-width, initial-scale=1.0" />
        <meta name="author" content="Mazzini Kelian" />
        <meta name="description" content="Page web du serveur de fichiers Mazzini" />

        <title>Mazzini Cloud</title>
        <link rel="shortcut icon" href="/image/mazzinicon.png" type="image/png"/>
        <link rel="stylesheet" href="/reset.css" />
        <link rel="stylesheet" media="screen" href="/style.css" />
    </head>
    <header>
        <h1>Mazzini Cloud</h1>
        <#if isLogged>
            <p>connecté en tant que: ${username}</p>
            <#else>
                <p>${username}</p>
        </#if>
        <nav id="headNav">
            <ul>
                <li id="linkNav"><a href="/">Acceuil</a></li>
                <li id="linkNav"><a href="/sujet">A propos</a></li>
                <#if isLogged>
                    <li id="linkNav"><a href="/logout">Déconnexion</a></li>
                    <#else>
                        <li id="linkNav"><a href="/register">S'inscrire</a></li>
                        <li id="linkNav"><a href="/login">Connexion</a></li>
                </#if>
                <#if isAdmin>
                    <li id="linkNav"><a href="/admin">Admin</a></li>
                </#if>             
            </ul>
        </nav>     
    </header>
</html>