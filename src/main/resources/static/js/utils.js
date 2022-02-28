/**
 * HTTP POST
 * @param url
 * @param inputData
 * @param result
 */
function doPost(url, inputData, result) {
    var request = $.ajax({
        url: url,
        type: 'POST',
        dataType: 'json',
        contentType: 'application/json',
        data: inputData
    });

    request.done(result);
    request.fail(function (xhr, textStatus) {
        alert("POST 통신 실패 " + textStatus);
    });
}