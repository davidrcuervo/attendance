<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<jsp:include page="/WEB-INF/view/web/styles/attendance.jsp"></jsp:include>
<c:set scope="request" var="active_link">event</c:set>
<c:set scope="request" var="content">
	<h2 class="text-center">Please select and event to check the assitance.</h2>
	<div class="row">
		<c:forEach var="event" items="${user.events}">
			<div class="${layout_grid_no_left_menu}">
				<div class="thumbnail">
					<div class="caption">
						<h1 class="text-center"><span class="glyphicon glyphicon-calendar"></span></h1>
						<h3 class="text-center">${event.name}</h3>
						<p>
							<a href="${pageContext.request.contextPath}/event/${event.urlEncodedName}" class="btn btn-primary btn-block" role="button">Select</a> 
						</p>
					</div>
				</div>
			</div>
		</c:forEach>
		<div class="${layout_grid_no_left_menu}">
			<div class="thumbnail">
				<div class="caption">
					<h1 class="text-center"><span class="glyphicon glyphicon-plus"></span></h1>
					<h3 class="text-center">Add new Event</h3>
					<p>
						<a href="${pageContext.request.contextPath}/event/add" class="btn btn-primary btn-block" role="button"><span class="glyphicon glyphicon-plus"></span>  Add new Event</a> 
					</p>
				</div>
			</div>
		</div>
	</div>
</c:set>
<jsp:include page="/WEB-INF/view/templates/web.jsp"></jsp:include>