<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<c:set var="loginError" scope="request">Sorry, you're username or password are wrong. Please try again</c:set>

<jsp:include page="/WEB-INF/view/web/login.jsp" />