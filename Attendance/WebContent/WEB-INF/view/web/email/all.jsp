<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<c:set scope="request" var="content">
	<div class="row">
		<c:forEach var="email" items="${user.emails}">
			<div class="col-xs-12 col-sm-6 col-md-4 col-lg-2">
				<div class="caption">
					<h1 class="text-center"><span class="glyphicon glyphicon-envelope"></span></h1>
					<h3 class="text-center">${email.name}</h3>
					<p>
						<a href="${pageContext.request.contextPath}/email/${email.url}" class="btn btn-primary btn-block" role="button">Select</a>
						<a href="${pageContext.request.contextPath}/email/delete/${email.url}?url=email" class="btn btn-default btn-block" role="button">Delete</a> 
					</p>
				</div>
			</div>
		</c:forEach>
		<div class="col-xs-12 col-sm-6 col-md-4 col-lg-2">
			<div class="thumbnail">
				<div class="caption">
					<h1 class="text-center"><span class="glyphicon glyphicon-plus"></span></h1>
					<h3 class="text-center">Add new Email</h3>
					<p>
						<a href="${pageContext.request.contextPath}/email/add" class="btn btn-primary btn-block" role="button"><span class="glyphicon glyphicon-plus"></span>  Add new Email</a> 
					</p>
				</div>
			</div>
		</div>
	</div>	
</c:set>

<c:set scope="request" var="active_link">emails</c:set>
<jsp:include page="/WEB-INF/view/templates/web.jsp"></jsp:include>