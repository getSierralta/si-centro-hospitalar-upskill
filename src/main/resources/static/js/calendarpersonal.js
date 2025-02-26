let nav = 0; // what month we're on

const calendar = document.getElementById('calendar');
const backDrop = document.getElementById('modalBackDrop');
const selectedDay = document.getElementById('selectedDay');
const weekdays = ['Sunday', 'Monday', 'Tuesday', 'Wednesday', 'Thursday', 'Friday', 'Saturday'];
let lastDaySquare = null;
const dt = new Date();
const popUp = document.getElementById("popup");
const popUpContent = document.getElementById("popup__content");
let id = null;
const url = window.location.href;
const st = url.split("/");
const especialidade = st[5];
const content = document.getElementById('content');

function openModal(monthName, daySquare, month){
    if(lastDaySquare != null){
        lastDaySquare.classList.remove('today');
        lastDaySquare = null;
    }    
    lastDaySquare = daySquare;
    selectedDay.innerText = "";
    selectedDay.innerText = daySquare.innerText+ ", "+monthName;
    daySquare.classList.add('today');

    
    const day = daySquare.innerText;
    const dia = "2021-"+month.toString()+"-"+day.toString();
    const vagas = document.getElementById("vagas");
    vagas.innerHTML = "";
    fetch(`http://localhost:8080/utente/calendarutente/${dia}`)
    .then(response => response.json())
    .then(data =>         
            data.forEach(element => { 
                const sidebar__list = document.createElement('li'); 
                sidebar__list.classList.add('sidebar__list-item');

                const time = document.createElement('span'); 
                time.classList.add('list-item__time');
                time.innerText = element.time;

                const title = document.createElement('span'); 
                title.classList.add('list-item__title');
                title.innerText = element.especialidade;

                const button = document.createElement('button'); 
                button.innerText = "Ver Consulta";

                const icon = document.createElement('div'); 
                icon.classList.add('icon');
                icon.classList.add('consulta');

                button.appendChild(icon);
                
                button.addEventListener('click', () => {
                    abrirPopUp(element);
                });

                sidebar__list.appendChild(time);
                sidebar__list.appendChild(title);
                sidebar__list.appendChild(button);
                vagas.appendChild(sidebar__list);
            })
        );  
}

function abrirPopUp(appointment){
    //popup
    
    popUp.style.visibility = 'visible';
    popUp.style.opacity = '1';
    
    popUpContent.style.opacity = '1';
    popUpContent.style.transform = 'translate(-50%,-50%) scale(1)';
    
  
    //content 
    
    content.innerHTML = "";
    const title = document.createElement('p'); 
    title.innerText = "A sua consulta: ";
    const id2 = document.createElement('p'); 
    id2.innerText = "Id: "+appointment.id;
    const ti = appointment.date.split("T");
    const date = document.createElement('p'); 
    date.innerText = "Data: "+ti[0];
    const time = document.createElement('p'); 
    time.innerText = "Hora: "+appointment.time;
    const es = document.createElement('p'); 
    es.innerText = "Especialidade: "+appointment.especialidade;
    const medico = document.createElement('p'); 
    medico.innerText = "Medico: "+appointment.doctor.name;
    const status = document.createElement('p'); 
    status.innerText = "Status: "+appointment.status;

    content.appendChild(title);
    content.appendChild(id2);
    content.appendChild(date);
    content.appendChild(time);
    content.appendChild(es);
    content.appendChild(medico);
    content.appendChild(status);

    const flex = document.getElementById("buttons");
    flex.innerHTML = "";

    const fecharConsulta = document.createElement('button');
    fecharConsulta.id = "cancelarConsulta";
    fecharConsulta.addEventListener('click', closePopUp);
    fecharConsulta.innerText = "fechar";
    const cancelarConsulta = document.createElement('button');
    cancelarConsulta.id = "cancelarConsulta";
    cancelarConsulta.addEventListener('click',  () => {
        id = null;
        id = appointment.id;
        const url = `http://localhost:8080/utente/cancelar/${id}`;
        fetch(url)
        .then(response => response.status == 500 ? giveError2() : response.json())
        .then(data =>  { 
                    content.innerHTML = "";
                    const success = document.createElement('img'); 
                    success.classList.add('inversed');
                    success.src = "/img/boa-contrictor-36.svg";
                    success.style.maxHeight = "80%";
                    content.appendChild(success);
                }
            ); 
    });
    cancelarConsulta.innerText = "Cancelar consulta";
    const checkIn = document.createElement('button');
    checkIn.id = "checkIn";
    checkIn.addEventListener('click',  () => {
        id = null;
    id = appointment.id;
    const url = `http://localhost:8080/utente/checkin/${id}`;
    fetch(url)
    .then(response => response.status == 500 ? giveError() : response.json())
    .then(data =>         
            data.forEach(element => { 
                content.innerHTML = "";               
                const senha = document.createElement('div'); 
                senha.innerText = element.numeroSenha;
                senha.classList.add('tracknumberutente__number');
                senha.style.maxHeight = "65%";
                const title = document.createElement('p'); 
                const b = document.createElement('b'); 
                b.innerText = "Numero de senha: ";
                senha.classList.add('tracknumberutente__number');
                title.appendChild(b);
                content.appendChild(title);
                content.appendChild(senha);
            })
        );  
    });  
    checkIn.innerText = "Registrar chegada";
    flex.appendChild(cancelarConsulta);
    flex.appendChild(checkIn);
    flex.appendChild(fecharConsulta);
    
}




function giveError(){
    content.innerHTML = "";
    const error = document.createElement('img'); 
    error.classList.add('inversed');
    error.src = "/img/jenipurr-chile-29.svg";
    error.style.maxHeight = "65%";
    // div.appendChild(error);    
    const error2 = document.createElement('p'); 
    error2.innerText = "Olha sinceiramente tipo um de dois né? Ou não é hoje o já pediste uma senha, nao é por questionar a tua inteligencia nem nada é só uma informação, se lhe serve a carapuça né";
    content.appendChild(error2);    
}

function giveError2(){
    content.innerHTML = "";
    const error = document.createElement('img'); 
    error.classList.add('inversed');
    error.src = "/img/jenipurr-chile-29.svg";
    error.style.maxHeight = "65%";
    content.appendChild(error);     
}

function closePopUp(){
    popUp.style.visibility = 'hidden';
    popUp.style.opacity = '0';
    
    popUpContent.style.opacity = '0';
    popUpContent.style.transform = 'translate(-50%, -50%) scale(.25)';
}

function load(){  
    
    //reset the calendar
    calendar.innerHTML = '';
    
    dt.setMonth(new Date().getMonth() + nav);


   const day = dt.getDate();
   const month = dt.getMonth();
   const year = dt.getFullYear();

   const fisrtDayOfMonth = new Date(year, month, 1);
   //0 is the last day of the previous month
   const daysInMonth = new Date(year, month+1, 0).getDate();

   //figuring out the day of the week
    const dateString = fisrtDayOfMonth.toLocaleDateString('en-us', {
        weekday: 'long',
        year: 'numeric',
        month: 'numeric',
        day: 'numeric',
    });

    //calculate the day that are not from this month
    const paddingDays = weekdays.indexOf(dateString.split(', ')[0]);

    const monthName = dt.toLocaleDateString('en-us', {month: 'long'});
    document.getElementById('monthDisplay').innerText = `${monthName} ${year}`;

    
    let week = new Array();
    for(let i = 1; i <= paddingDays + daysInMonth; i++){

        const daySquare = document.createElement('div');
        daySquare.classList.add('calendar__day');
        const dateSquare = document.createElement('span');
        dateSquare.classList.add('calendar__date');
        daySquare.appendChild(dateSquare);
        //const dayString = `${month + 1}/${i - paddingDays}/${year}`;
        if(i > paddingDays){
            dateSquare.innerText = i - paddingDays;
            const d = year + "-"+month+ "-"+dateSquare.innerText;
            fetch(`http://localhost:8080/utente/calendarutente/${d}`)
            .then(response => response.json())
            .then(data => data.length === 0 ? daySquare.classList.add('full') : daySquare.classList.add('color-green'));  
        }else{
            daySquare.classList.add('inactive');
        }
        daySquare.addEventListener('click', () => openModal(monthName, daySquare, month));
        
        week.push(daySquare);
        if(i === paddingDays + daysInMonth){
            for(var j = week.length; j < 7; j++){
                const daySquare2 = document.createElement('div');
                daySquare2.classList.add('calendar__day');
                const dateSquare2 = document.createElement('span');
                dateSquare2.classList.add('calendar__date');
                daySquare2.appendChild(dateSquare2);
                daySquare2.classList.add('inactive');
                week.push(daySquare2);
            }
            newWeek(week);
            week = [];
        }
        if(i % 7 === 0){
            newWeek(week);
            week = [];
        }
    }
}

function newWeek(week){
    const week2 = document.createElement('div');
    week2.classList.add('calendar__week');
    for (var i = 0; i < week.length; i++){
        week2.appendChild(week[i]);
     }
    calendar.appendChild(week2);
}

function initButtons(){
    document.getElementById('nextButton').addEventListener('click', () => {
        nav++;
        load();
    });
    document.getElementById('backButton').addEventListener('click', () => {
        nav--;
        load();
    });
}

initButtons();
load();