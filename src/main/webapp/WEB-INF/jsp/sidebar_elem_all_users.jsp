<li id='user_<%= request.getParameter("userId") %>'>
    <%= request.getParameter("name") %>
    <input type = 'button' id = 'follow_<%= request.getParameter("userId") %>'
    value = '<%= request.getParameter("followStatus") %>' onclick='changeFollowStatus(<%= request.getParameter("userId") %>)' />
</li>
