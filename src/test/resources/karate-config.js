function fn() {

    var config = {
        baseUrl : getUrl()
    };

    return config;
}

function getUrl() {
    var url = java.lang.System.getProperty('KARATE_BASE_URL');
    karate.log("Property:" + url)
    if (url === null || url.length < 1) url = java.lang.System.getenv('KARATE_BASE_URL');
    karate.log("Env:" + url)
    if (url === null || url.length < 1) url = 'http://localhost:8080';
    karate.log("Final:" + url)
    return url
}