// Copyright 2019 Google LLC
//
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
//
//     https://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.

/**
 * Adds server content to page
 */

function loadPage(){
    checkLogin();
    getComments();
    
}
function checkLogin(){
    fetch('/check').then(function(response){
        if(response.status==200){
           document.getElementById("commentHere").style.visibility = "visible";
        }
        if(response.status==403){
           document.getElementById("commentHere").style.visibility = "hidden";
           const commentFormElement = document.getElementById('submitComment');
          commentFormElement.innerHTML = '';
          const aElement = document.createElement('a');
          var link = document.createTextNode("Login Here");
          aElement.appendChild(link);  
          aElement.href=('/check');
          aElement.title="Login Here"
          commentFormElement.appendChild(aElement);
        }
    });

}
function getComments() {
    var queryString = window.location.search;
    console.log(queryString);    
    const urlParams = new URLSearchParams(queryString);
    var maxComments = urlParams.get('quantity')
    if(maxComments==null){
        maxComments=5;
    }
    console.log(maxComments);
  fetch('/comment?maxComments='+ maxComments).then(response => response.json()).then((comments) => {

    const commentListElement = document.getElementById('comment-container');
    commentListElement.innerHTML = '';
    comments.forEach((line) => {
      commentListElement.appendChild(createListElement(line));
   });
  });
}

/** Creates an <li> element containing text. */
function createListElement(text) {
  const liElement = document.createElement('li');
  liElement.innerText = text;
  return liElement;
}

function deleteComments(){
    fetch('/delete').then(response => response.json()).then((comments) => {

    const commentListElement = document.getElementById('comment-container');
    commentListElement.innerHTML = '';
    comments.forEach((line) => {
      commentListElement.appendChild(createListElement(line));
   });
  });
}
/**
 * Handles response by converting it to text and passing the result to
 * addQuoteToDom().
 */
function handleResponse(response) {
  console.log('Handling the response.');

  // response.text() returns a Promise, because the response is a stream of
  // content and not a simple variable.
  const textPromise = response.text();

  // When the response is converted to text, pass the result into the
  // addQuoteToDom() function.
  textPromise.then(addCommentToDom);
}

function addCommentToDom(comment) {
  console.log('Adding greeting to dom: ' + comment);

  const commentContainer = document.getElementById('comment-container');
  commentContainer.innerText = comment;
}

function createMap() {
  const map = new google.maps.Map(
      document.getElementById('map'),
      {center: {lat: 37.422, lng: -122.084}, zoom: 16});
}