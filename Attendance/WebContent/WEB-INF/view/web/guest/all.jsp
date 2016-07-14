<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:set scope="request" var="active_link">guests</c:set>

<c:set scope="request" var="content">
	<div class="table-responsive">
		<table class="table table-striped">
			<tr class="info">
				<th>Name</th>
				<th>e-Mail</th>
				<th></th>
			</tr>
			<c:forEach var="guest" items="${guests}">
				<tr>
					<td>${guest.name}</td>
					<td>${guest.email}</td>
					<td><a href="${pageContext.request.contextPath}/guest/${guest.urlEncodedEmail}" class="btn btn-default">View</a></td>
				</tr>
			</c:forEach>
		</table>
	</div>
</c:set>

<jsp:include page="/WEB-INF/view/templates/web.jsp"></jsp:include>