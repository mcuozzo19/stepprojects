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
function getGreeting() {
  fetch('/data').then(response => response.json()).then((greetings) => {
    // stats is an object, not a string, so we have to
    // reference its fields to create HTML content

    const greetingListElement = document.getElementById('greeting-container');
    greetingListElement.innerHTML = '';
    greetingListElement.appendChild(
        createListElement('Greeting1: ' + greetings[0]));
    greetingListElement.appendChild(
        createListElement('Greeting2: ' + greetings[1]));
    greetingListElement.appendChild(
        createListElement('Greeting3: ' + greetings[2]));
  });
}

/** Creates an <li> element containing text. */
function createListElement(text) {
  const liElement = document.createElement('li');
  liElement.innerText = text;
  return liElement;
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
  textPromise.then(addGreetingToDom);
}

/** Adds a random quote to the DOM. */
function addGreetingToDom(greeting) {
  console.log('Adding greeting to dom: ' + greeting);

  const quoteContainer = document.getElementById('greeting-container');
  quoteContainer.innerText = greeting;
}