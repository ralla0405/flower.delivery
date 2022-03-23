/**
 * HTTP POST
 * @param url
 * @param inputData
 * @param result
 */
function doPost(url, inputData, result) {
    var token = $("meta[name=_csrf]").attr("content");
    var header = $("meta[name=_csrf_header]").attr("content");
    $(document).ajaxSend(function (e, xhr, options) { xhr.setRequestHeader(header, token)});
    var request = $.ajax({
        url: url,
        type: 'POST',
        dataType: 'json',
        contentType: 'application/json',
        data: JSON.stringify(inputData)
    });

    request.done(result);
    request.fail(function (xhr, textStatus) {
        alert("POST 통신 실패 " + textStatus);
    });
}