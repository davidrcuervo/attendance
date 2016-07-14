<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<c:set scope="request" var="content">
	<h3 class="text-center">${email.name}</h3>
	<div>${email.email}</div>
	<a href="${pageContext.request.contextPath}/email/edit/${email.url}" class="btn btn-block btn-default"><span class="glyphicon glyphicon-pencil"></span> Edit</a>
	<a href="${pageContext.request.contextPath}/email" class="btn btn-block btn-danger"><span class="glyphicon glyphicon-remove"></span> Close</a>
</c:set>

<jsp:include page="/WEB-INF/view/templates/web.jsp"></jsp:include> 