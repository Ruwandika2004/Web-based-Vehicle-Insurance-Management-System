document.addEventListener('DOMContentLoaded',function(){
  document.querySelectorAll('.menu a').forEach(a=>a.addEventListener('click',e=>{
    document.querySelectorAll('.menu a').forEach(x=>x.classList.remove('active'));
    a.classList.add('active');
    e.preventDefault();
  }))

  // simple demo interaction: open first card on click
  document.querySelectorAll('.card').forEach(card=>card.addEventListener('click',()=>{
    card.style.transform = 'translateY(-4px)';
    setTimeout(()=>card.style.transform='none',220);
  }))
})