<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="header.jspf" %>
<c:set var="editing" value="${not empty book}"/>
<div class="row justify-content-center"><div class="col-md-7">
  <div class="card shadow-sm"><div class="card-body p-4">
    <h3 class="mb-3">${editing ? 'Edit Book' : 'Add Book'}</h3>
    <form method="post" action="${pageContext.request.contextPath}/books">
      <input type="hidden" name="action" value="save">
      <input type="hidden" name="id" value="${editing ? book.id : ''}">
      <div class="mb-3"><label class="form-label">Title</label>
        <input name="title" class="form-control" required value="<c:out value='${book.title}'/>"></div>
      <div class="mb-3"><label class="form-label">Author</label>
        <input name="author" class="form-control" required value="<c:out value='${book.author}'/>"></div>
      <div class="row">
        <div class="col-md-6 mb-3"><label class="form-label">Category</label>
          <input name="category" class="form-control" required value="<c:out value='${book.category}'/>"></div>
        <div class="col-md-6 mb-3"><label class="form-label">Format</label>
          <select name="type" id="type" class="form-select" onchange="updateLabel()">
            <option value="PRINTED" ${book.format eq 'PRINTED' ? 'selected' : ''}>Printed book</option>
            <option value="EBOOK" ${book.format eq 'EBOOK' ? 'selected' : ''}>E-book</option>
          </select></div>
      </div>
      <div class="row">
        <div class="col-md-4 mb-3"><label class="form-label">Price (Rs.)</label>
          <input name="price" type="number" step="0.01" min="0" class="form-control" required value="${editing ? book.price : ''}"></div>
        <div class="col-md-4 mb-3"><label class="form-label">Stock</label>
          <input name="stock" type="number" min="0" class="form-control" required value="${editing ? book.stock : ''}"></div>
        <div class="col-md-4 mb-3"><label class="form-label" id="extraLabel">Weight (g)</label>
          <input name="extra" type="number" step="0.1" min="0" class="form-control" required
                 value="${editing ? book.extraValue : ''}"></div>
      </div>
      <button class="btn btn-dark">${editing ? 'Update' : 'Save'}</button>
      <a class="btn btn-outline-secondary" href="${pageContext.request.contextPath}/books">Cancel</a>
    </form>
  </div></div>
</div></div>
<script>
  function updateLabel() {
    document.getElementById('extraLabel').textContent =
      document.getElementById('type').value === 'EBOOK' ? 'File size (MB)' : 'Weight (g)';
  }
  updateLabel();
</script>
<%@ include file="footer.jspf" %>
