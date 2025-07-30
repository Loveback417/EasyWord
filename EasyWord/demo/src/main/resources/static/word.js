// 显示加载状态
function showLoading() {
    document.getElementById('loadingIndicator').style.display = 'block';
}

// 隐藏加载状态
function hideLoading() {
    document.getElementById('loadingIndicator').style.display = 'none';
}

// 获取CSRF令牌（Spring Security需要）
function getCsrfToken() {
    return document.querySelector('meta[name="_csrf"]').content;
}

// 初始化页面
document.addEventListener('DOMContentLoaded', function() {
    // 删除单词确认
    document.addEventListener('click', function(e) {
        if (e.target.classList.contains('btn-delete')) {
            e.preventDefault();
            const wordId = e.target.getAttribute('data-id');

            if (confirm('确定要删除这个单词吗？')) {
                showLoading();
                fetch(`/words/delete/${wordId}`, {
                    method: 'GET',
                    headers: {
                        'X-CSRF-TOKEN': getCsrfToken()
                    }
                })
                    .then(response => {
                        if (response.ok) {
                            window.location.reload();
                        } else {
                            alert('删除失败');
                        }
                    })
                    .catch(error => console.error('Error:', error))
                    .finally(hideLoading);
            }
        }
    });

    // 编辑单词
    document.addEventListener('click', function(e) {
        if (e.target.classList.contains('edit-btn')) {
            const wordId = e.target.getAttribute('data-id');
            showLoading();

            fetch(`/words/${wordId}`)
                .then(response => response.json())
                .then(word => {
                    // 填充表单
                    document.querySelector('input[name="id"]').value = word.id;
                    document.querySelector('input[name="word"]').value = word.word;
                    document.querySelector('input[name="kind"]').value = word.kind;
                    document.querySelector('textarea[name="meaning"]').value = word.meaning;
                    document.querySelector('textarea[name="sentence"]').value = word.sentence;
                    document.querySelector('textarea[name="sentenceMeaning"]').value = word.sentenceMeaning;
                    document.querySelector('input[name="difficulty"]').value = word.difficulty;

                    // 滚动到表单
                    document.querySelector('.form-section').scrollIntoView({
                        behavior: 'smooth'
                    });
                })
                .catch(error => console.error('Error:', error))
                .finally(hideLoading);
        }
    });

    // AJAX搜索
    const searchForm = document.getElementById('searchForm');
    if (searchForm) {
        searchForm.addEventListener('submit', function(e) {
            e.preventDefault();
            const keyword = document.getElementById('searchKeyword').value.trim();
            showLoading();

            fetch(`/words/search?keyword=${encodeURIComponent(keyword)}`)
                .then(response => response.text())
                .then(html => {
                    const parser = new DOMParser();
                    const doc = parser.parseFromString(html, 'text/html');
                    document.getElementById('wordTableBody').innerHTML =
                        doc.getElementById('wordTableBody').innerHTML;
                })
                .catch(error => console.error('Error:', error))
                .finally(hideLoading);
        });
    }

    // 重置搜索
    const resetSearch = document.getElementById('resetSearch');
    if (resetSearch) {
        resetSearch.addEventListener('click', function() {
            document.getElementById('searchKeyword').value = '';
            window.location.href = '/words';
        });
    }

    // 表单提交处理
    const wordForm = document.getElementById('wordForm');
    if (wordForm) {
        wordForm.addEventListener('submit', function() {
            const submitBtn = this.querySelector('button[type="submit"]');
            submitBtn.disabled = true;
            submitBtn.innerHTML = '<span class="spinner-border spinner-border-sm"></span> 保存中...';
        });
    }

    // 导出CSV
    const exportBtn = document.getElementById('exportBtn');
    if (exportBtn) {
        exportBtn.addEventListener('click', function() {
            showLoading();
            fetch('/words/export')
                .then(response => response.blob())
                .then(blob => {
                    const url = window.URL.createObjectURL(blob);
                    const a = document.createElement('a');
                    a.href = url;
                    a.download = 'words_export.csv';
                    document.body.appendChild(a);
                    a.click();
                    window.URL.revokeObjectURL(url);
                })
                .catch(error => console.error('Error:', error))
                .finally(hideLoading);
        });
    }
});

// 错误处理
window.addEventListener('error', function(e) {
    console.error('Error:', e.error);
    hideLoading();
    alert('操作失败，请查看控制台日志');
});