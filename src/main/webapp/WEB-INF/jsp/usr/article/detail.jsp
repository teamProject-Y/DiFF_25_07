<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<c:set var="pageTitle" value="ARTICLE DETAIL"></c:set>
<%@ include file="../common/head.jspf"%>


<script>
	const params = {};
	params.id = parseInt('${param.id}');

</script>

<script>
			return;
		}
	}

				console.log(data);
					}

				console.log(data);
	}

}

			id : params.id,
			ajaxMode : 'Y'
		}, function(data) {
			console.log(data);
		}, 'json');
	}

	$(function() {

	})
</script>

						</button>

						</div>
			<c:if test="${article.userCanModify }">
			</c:if>
			<c:if test="${article.userCanDelete }">
			</c:if>
		</div>

	</div>
		




							</c:if>
							</c:if>
				</c:forEach>
				</c:if>
	</div>



<%@ include file="../common/foot.jspf"%>