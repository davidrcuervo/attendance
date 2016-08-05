<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<jsp:include page="/WEB-INF/view/web/forms/email_server.jsp"></jsp:include>

<c:set scope="request" var="content">
	<h3 class="text-center">Email Server Settings</h3>
	<div>${email_server_form}</div>
</c:set>
<jsp:include page="/WEB-INF/view/templates/web.jsp"></jsp:include>