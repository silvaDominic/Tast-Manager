$(document).ready(function() {
    $('.dropdate').dropdate({
        dateFormat: 'mm-dd-yyyy'
    });

    // Cache fields
    var $tasks = $('#tasks');
    var $target_date = $('.target_date');
    var $task_container = $('.task_container')

// ------------------------------------------- AJAX REQUESTS -----------------------------------------------------------

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
        var $form = $(this);
        var taskToPost = JSON.stringify($form.serializeData());
        console.log(taskToPost);

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
            data: taskToPost
        }).done(function(response, textStatus, jqXHR){
            insertTask($.parseJSON(response));
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

    // AJAX call for getting current tasks
    $.ajax({
        url: '/tasks',
        type: 'GET'
    }).done(function(response, textStatus, jqXHR){
        $.each($.parseJSON(response), function(i, task){
            insertTask(task);
            });
        console.log("Get successful.");
        console.log("Response: " + response);
        console.log("Text Status: " + textStatus);
        console.log("JQ XMLHttpReq: " + jQuery.parseJSON(jqXHR.responseText));
    }).fail(function(jqXHR, textStatus, errorThrown){
        console.log("Error.");
        console.log("Text Status: " + textStatus);
        console.log("JQ XMLHttpReq: " + jQuery.parseJSON(jqXHR.responseText));
    });

    // Handler for deleting selected tasks
    $tasks.delegate('.remove', 'click', function() {
        // Cache task to delete <li>
        var $taskToDelete = $(this).closest('li');
        console.log($taskToDelete);

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

    // Updates checkboxes
    $tasks.delegate('.status', 'click', function(){
        var task_to_update = JSON.stringify($(this).serializeData());
        console.log(task_to_update);

        $.ajax({
            url: '/tasks/' + $(this).attr('data-id'),
            type: 'PUT',
            data: task_to_update
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
    });

    // Enable editing of task
    $tasks.delegate('.update_task', 'click', function(){
       var $task_description = $(this).find('.task_description');
       // TODO: Successfully send updated form
       // TODO: Prevent multiple clicks when already on element
       var $updated_form = $(this).find('.update_form').serializeData();
       console.log($task_description);
       console.log($updated_form);
       $task_description.prop('disabled', false);

/*       $(this).find('.update').on('click', function(){
          $.ajax({
              url: '/tasks/' + $(this).attr('data-id'),
              type: 'PUT',
              data: $updated_form
          }).done(function(response, textStatus, jqXHR){
             $task_description.prop('disabled', true);
              console.log("Put successful.");
              console.log("Response: " + response);
              console.log("Text Status: " + textStatus);
              console.log("JQ XMLHttpReq: " + jQuery.parseJSON(jqXHR.responseText));
          }).fail(function(jqXHR, textStatus, errorThrown){
              console.log("Error.");
              console.log("Text Status: " + textStatus);
              console.log("JQ XMLHttpReq: " + jQuery.parseJSON(jqXHR.responseText));
          });
       });*/
    });


// ---------------------------------------- HELPER FUNCTIONS -----------------------------------------------------------

    // Uses the mustache template engine to dynamically insert tasks into DOM
    function insertTask(data){
        $.Mustache.load('templates/tasks.html', function() {
            $('#tasks').mustache('main_form', data);
        });
    }

    // Serializes and constructs form data into JSON object
    $.fn.serializeData = function()
    {
        var o = {};
        var a = this.serializeArray({ checkboxesAsBools: true });
        console.log(a);

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

    (function ($) {

         $.fn.serialize = function (options) {
             return $.param(this.serializeArray(options));
         };

         $.fn.serializeArray = function (options) {
             var o = $.extend({
             checkboxesAsBools: false
         }, options || {});

         var rselectTextarea = /select|textarea/i;
         var rinput = /text|hidden|password|search/i;

         return this.map(function () {
             return this.elements ? $.makeArray(this.elements) : this;
         })
         .filter(function () {
             return this.name && !this.disabled &&
                 (this.checked
                 || (o.checkboxesAsBools && this.type === 'checkbox')
                 || rselectTextarea.test(this.nodeName)
                 || rinput.test(this.type));
             })
             .map(function (i, elem) {
                 var val = $(this).val();
                 return val == null ?
                 null :
                 $.isArray(val) ?
                 $.map(val, function (val, i) {
                     return { name: elem.name, value: val };
                 }) :
                 {
                     name: elem.name,
                     value: (o.checkboxesAsBools && this.type === 'checkbox') ? //moar ternaries!
                            (this.checked ? 'true' : 'false') :
                            val
                 };
             }).get();
         };

    })(jQuery);
});