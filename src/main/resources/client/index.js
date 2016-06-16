$(document).ready(function() {
    $('.dropdate').dropdate({
        dateFormat: 'mm/dd/yyyy'
    });

    var request;
    function addTask(task){
        $
    }

    $.fn.serializedData = function()
    {
        var o = {};
        var a = this.serializeArray();
        $.each(a, function() {
            if (o[this.name] !== undefined) {
                if (!o[this.name].push) {
                    o[this.name] = [o[this.name]];
                }
                o[this.name].push(this.value || '');
            } else {
                o[this.name] = this.value || '';
            }
        });
        return o;
    };

    // Binds submit event from form
    $("#task_form").submit(function(event){

        // Prevent form from submitting from browser
        event.preventDefault();

        // Serialize form data
        $form = $(this);

        $JSON = JSON.stringify($form.serializedData());
        console.log($JSON);

        // Aborts any pending requests
        if (request) {
            request.abort();
        }

        // Selects and caches input field
        var $inputs = $form.find("input");

        // Briefly disables input fields during duration of AJAX request
        $inputs.prop("disabled", true);

        // AJAX requests
        $.ajax({
            url: $form.attr('action'),
            type: $form.attr('method'),
            data: $JSON
        }).done(function(response, textStatus, jqXHR){
            console.log("Post successful.");
            console.log("Response: " + response);
            console.log("Text Status: " + textStatus);
            console.log("JQ XMLHttpReq: " + jQuery.parseJSON(jqXHR.responseText));
        }).fail(function(jqXHR, textStatus, errorThrown){
            console.log("Error.");
            console.log("Response: " + response);
            console.log("Text Status: " + textStatus);
            console.log("JQ XMLHttpReq: " + jQuery.parseJSON(jqXHR.responseText));
        }).always(function(){
            $inputs.prop("disabled", false);;
        });
    });
});