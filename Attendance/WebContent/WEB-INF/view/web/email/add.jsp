<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<c:set scope="request" var="button_email_form"><span class="glyphicon glyphicon-floppy-disk"></span> Save</c:set>
<c:set scope="request" var="submit_value">addEmail</c:set>
<c:set scope="request" var="cancel_link">${pageContext.request.contextPath}/email</c:set>
<jsp:include page="/WEB-INF/view/web/forms/email.jsp"></jsp:include>

<c:set scope="request" var="content">
	<h3 class="text-center">Add email</h3>
	<div>${email_form}</div>
</c:set>

<c:set scope="request" var="title">Add Email</c:set>
<jsp:include page="/WEB-INF/view/templates/web.jsp"></jsp:include>