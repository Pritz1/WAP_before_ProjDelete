<!DOCTYPE html>
<html>

<head>
  <script data-require="jquery@*" data-semver="3.1.1" src="https://ajax.googleapis.com/ajax/libs/jquery/3.1.1/jquery.min.js"></script>
  <script data-require="datatables@*" data-semver="1.10.12" src="//cdn.datatables.net/1.10.12/js/jquery.dataTables.min.js"></script>
  <script src="dataTables.rowReorder.js"></script>
  <link data-require="datatables@*" data-semver="1.10.12" rel="stylesheet" href="//cdn.datatables.net/1.10.12/css/jquery.dataTables.min.css" />
  <link rel="stylesheet" href="//cdn.datatables.net/rowreorder/1.2.0/css/rowReorder.dataTables.min.css" />
  <link rel="stylesheet" type="text/css" href="https://maxcdn.bootstrapcdn.com/font-awesome/4.7.0/css/font-awesome.min.css">


<style>
div.addRow{
      line-height: 45px;
    background-color: #fff;
    padding-left: 10px;
    border-bottom: 1px solid;
    border-top: 1px solid #e5e5e5;}
</style>
</head>

<body>
  <table id="example" class="display" width="100%" cellspacing="0">
    <thead>
      <tr>
        <th>order</th>
        <th>name</th>
        <th>country</th>
        <th>action</th>
      </tr>
    </thead>
  </table>

  
  <table id="newRow" style="display:none">
    <tbody>
      <tr>
        <td>
          <select id="selectbasic" name="selectbasic" class="form-control">
            <option value="1">option 1</option>
            <option value="2">option 2</option>
            <option value="2">option 3</option>
          </select>
        </td>
        <td>DVap
        </td>
        <td>
          www</td>
        <td><i class="fa fa-pencil-square" aria-hidden="true"></i>
          <i class="fa fa-minus-square" aria-hidden="true"></i> </td>
      </tr>
    </tbody>
  </table>


  <script>
    $(document).ready(function() {

      var table;

      $("#example").on("mousedown", "td .fa.fa-minus-square", function(e) {
        table.row($(this).closest("tr")).remove().draw();
      })

      $("#example").on('mousedown.edit', "i.fa.fa-pencil-square", function(e) {

        $(this).removeClass().addClass("fa fa-envelope-o");
        var $row = $(this).closest("tr").off("mousedown");
        var $tds = $row.find("td").not(':first').not(':last');

        $.each($tds, function(i, el) {
          var txt = $(this).text();
          $(this).html("").append("<input type='text' value=\""+txt+"\">");
        });

      });

      $("#example").on('mousedown', "input", function(e) {
        e.stopPropagation();
      });

      $("#example").on('mousedown.save', "i.fa.fa-envelope-o", function(e) {
        
        $(this).removeClass().addClass("fa fa-pencil-square");
        var $row = $(this).closest("tr");
        var $tds = $row.find("td").not(':first').not(':last');
        
        $.each($tds, function(i, el) {
          var txt = $(this).find("input").val()
          $(this).html(txt);
        });
      });
      
      
       $("#example").on('mousedown', "#selectbasic", function(e) {
        e.stopPropagation();
      });
      

/*       var url = 'http://www.json-generator.com/api/json/get/ccTtqmPbkO?indent=2';
      table = $('#example').DataTable({
        ajax: url,
        rowReorder: {
          dataSrc: 'order',
          selector: 'tr'
        },
        columns: [{
          data: 'order'
        }, {
          data: 'place'
        }, {
          data: 'name'
        }, {
          data: 'delete'
        }]
      }); */
      
/*       $('#example').css('border-bottom', 'none');
    $('<div class="addRow"><button id="addRow">Add New Row</button></div>').insertAfter('#example');

      // add row
      $('#addRow').click(function() {
        //t.row.add( [1,2,3] ).draw();
        var rowHtml = $("#newRow").find("tr")[0].outerHTML
        console.log(rowHtml);
        table.row.add($(rowHtml)).draw();
      }); */
    });
  </script>
</body>

</html>