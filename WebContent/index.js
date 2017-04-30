$(document).ready(function(){
	$("#submit").click(function(){
		var search_type = "default";
		if (document.getElementById("pageRank").checked) {
			search_type = "pageRank";
		}
		var search_term = document.getElementById("search").value;
		var post_data = JSON.stringify({"type":search_type, "term":search_term});
		$.ajax({
			type:"POST",
			url:"./getresults",
			data:post_data,
			async: false,
			error: function(request) {
				alert("Connection error");
			},
			success: function(data) {
				var result = JSON.parse(data);
				var section = document.getElementById("main-section");
				section.innerHTML = "";
				var ul = document.createElement("ul");
				section.appendChild(ul);
				for(x in result) {
					var temp = document.createElement("li");
					temp.innerHTML += ('<a href="'+ result[x]["url"] +'" class="title">' + result[x]["title"]+'</a></br>');
					temp.innerHTML += ('<a href="'+ result[x]["url"] +'" class="url">' + result[x]["url"] + '</a></br>');
					temp.innerHTML += ('<p>id:'+ result[x]["id"] +'</p>');
					temp.innerHTML += ('<p>'+ result[x]["description"] +'</p>')
					ul.appendChild(temp);
				}
			}
		});
	});
});