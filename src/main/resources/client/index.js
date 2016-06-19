$(document).ready(function() {
    $('.dropdate').dropdate({
        dateFormat: 'mm/dd/yyyy'
    });

    // Cache fields
    var $tasks = $('#tasks');
    var $target_date = $('#target_date');
    var taskTemplate = "<li>{{name}} Finish by: {{targetDate}} <button data-id = '{{id}}' class = 'remove'>X</button> <input data-id = '{{id}}' class = 'status' type = 'checkbox'></li>";

    // Uses the mustache template engine to dynamically insert tasks into DOM
    function addTask(task){
        $tasks.append(Mustache.render(taskTemplate, task));
    }

    // Serializes and constructs form data into JSON object
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

    // AJAX call for getting current tasks
    $.ajax({
        url: '/tasks',
        type: 'GET'
    }).done(function(response, textStatus, jqXHR){
        $.each($.parseJSON(response), function(i, task){
            addTask(task);
        });
        console.log("Get successful.");
        console.log("Response: " + response);
        console.log("Text Status: " + textStatus);
        console.log("JQ XMLHttpReq: " + jQuery.parseJSON(jqXHR.responseText));
    }).fail(function(jqXHR, textStatus, errorThrown){
        console.log("Error.");
        console.log("Text Status: " + textStatus);
        console.log("JQ XMLHttpReq: " + jQuery.parseJSON(jqXHR.responseText));
    })

    // Handler for submitting form data
    // Binds submit event from form
    $("#task_form").submit(function(event){

        var request;
        var task_input = document.forms['task_form']['task'].value;

        // Check for blank form
        if (task_input == null || task_input == ""){
            alert("No task? Then I guess you don't need me!");
            return false;
        }

        // Prevent form from submitting from browser
        event.preventDefault();

        // Serialize form data
        $form = $(this);
        $JSON = JSON.stringify($form.serializedData());

        // Aborts any pending requests
        if (request) {
            request.abort();
        }
        // Selects and caches input field
        var $inputs = $form.find("input");

        // Briefly disables input fields during duration of AJAX request
        $inputs.prop("disabled", true);

        // AJAX request for posting new task
        $.ajax({
            url: $form.attr('action'),
            type: $form.attr('method'),
            data: $JSON
        }).done(function(response, textStatus, jqXHR){
            addTask($.parseJSON(response));
            console.log("Post successful.");
            console.log("Response: " + response);
            console.log("Text Status: " + textStatus);
            console.log("JQ XMLHttpReq: " + jQuery.parseJSON(jqXHR.responseText));
        }).fail(function(jqXHR, textStatus, errorThrown){
            console.log("Error.");
            console.log("Text Status: " + textStatus);
            console.log("JQ XMLHttpReq: " + jQuery.parseJSON(jqXHR.responseText));
        }).always(function(){
            $inputs.prop("disabled", false);;
        });
    });

    // Handler for deleting selected tasks
    $tasks.delegate('.remove', 'click', function() {
        // Cache task to delete <li>
        var $taskToDelete = $(this).closest('li');

        // AJAX request for deleting existing task
        $.ajax({
            url: '/tasks/' + $(this).attr('data-id'),
            type: 'DELETE'
        }).done(function(response, textStatus, jqXHR){
            // Remove from DOM
            $taskToDelete.remove();
            console.log("Delete successful.");
            console.log("Response: " + response);
            console.log("Text Status: " + textStatus);
            console.log("JQ XMLHttpReq: " + jQuery.parseJSON(jqXHR.responseText));
      }).fail(function(jqXHR, textStatus, errorThrown){
          console.log("Error.");
          console.log("Text Status: " + textStatus);
          console.log("JQ XMLHttpReq: " + jQuery.parseJSON(jqXHR.responseText));
      });
    });

    $tasks.delegate('.status', 'click', function(){

        var $status = $(this);
        console.log($status.serializedData());
        var $JSON = JSON.stringify($status.serializedData());
        console.log($JSON);

        $.ajax({
            url: '/tasks/' + $(this).attr('data-id'),
            type: 'PUT',
            data: $JSON
        }).done(function(response, textStatus, jqXHR){
            console.log("Put successful.");
            console.log("Response: " + response);
            console.log("Text Status: " + textStatus);
            console.log("JQ XMLHttpReq: " + jQuery.parseJSON(jqXHR.responseText));
        }).fail(function(jqXHR, textStatus, errorThrown){
            console.log("Error.");
            console.log("Text Status: " + textStatus);
            console.log("JQ XMLHttpReq: " + jQuery.parseJSON(jqXHR.responseText));
        });
    })
});