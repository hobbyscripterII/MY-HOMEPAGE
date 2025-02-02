$(function () {
  window.config = {
    dev : {
      apiUrl : 'http://127.0.0.1/',
      url : 'http://127.0.0.1:5500/'
      },
    prod : {
      apiUrl : 'https://a-p-i.kro.kr/',
      url : 'https://jy-home.kro.kr/'
      }
  };

  window.env = window.location.hostname == '127.0.0.1' ? 'dev' : 'prod';
  window.apiUrl = window.config[window.env].apiUrl;
  window.url = window.config[window.env].url;
})