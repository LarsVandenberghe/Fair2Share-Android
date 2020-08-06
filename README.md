# Fair2Share Android

#### SPLIT THE BILL

De Fair2Share app maakt het voorschieten van uitgaven op vakantie of bij familie makkelijker. Betalingen kunnen worden bijgehouden zodat iedereen op elk moment zijn schulden kan bekijken. Deze app is gelijkaardig aan [tricount](https://www.tricount.com/) en [splitwise](https://www.splitwise.com/).

Dankzij Fair2Share is het mogelijk om zonder zorgen bedragen te lenen of voor te schieten aan vrienden of familie. Het is de bedoeling dat iedereen een profiel aanmaakt en in dezelfde activiteit zit (bv skivakantie). Je kan mensen vriendschapsverzoeken sturen. Vrienden kun je later toevoegen bij de gewenste activiteiten. Je maakt een transactie aan door een naam te kiezen, bedrag op te geven, de persoon op te geven die betaald heeft en vrienden (al dan niet inclusief de betaler) te selecteren waarvoor deze betaling is voorgeschoten. Op het einde van de activiteit heb je duidelijk overzicht heeft wie er schulden heeft en wie er geld moet ontvangen.

De overige onderdelen van dit softwarepakket zijn hier terug te vinden:
* [Web applicatie](https://github.com/LarsVandenberghe/Fair2Share-Frontend)

* [Web API](https://github.com/LarsVandenberghe/Fair2Share-Backend)




## Aan De Slag

Onderstaande instructies gaan over het opzetten van het project op basis van de broncode.

### Vereisten

Om het project te kunnen gebruiken zal je <a href="https://developer.android.com/studio">Android Studio</a> nodig hebben.

### Installeren

Na het openen van het project in Android Studio, open je AVD Manager, bij Tools. Maak een nieuw Virtual Device. Kies een device. Download indien nodig de laatste Android versie (Huidige versie Q bij het schrijven van dit document, API level 29) en ga door. Selecteer het device boven in het dropdown menu en voer het project uit. 

### Login

Na het opstarten van de applicatie is het belangrijk dat er internet is! Er zal een scherm tevoorschijn komen dat vraagt om je in te loggen. Het is mogelijk om via de app een nieuw account te registreren, maar om wat data te hebben om mee te werken kan je het onderstaande account gebruiken:

Email:

```
test@hogent.be
```

Paswoord:

```
testPassword1
```

Het inloggen moet slechts enkel moeten gebeuren bij de eerste keer dat de applicatie wordt opgestart. Het device onthoudt dan je login gegevens. Als het inloggen is gelukt, krijg je een overzicht van alle activiteiten. 



## Testen uitvoeren

//TODO



## Deployment

**Deze app is niet te downloaden via Google Play!** Deze app maakt gebruik van een <a href="http://78.20.29.170:5000/swagger/">.NET Core backend</a> en is onderdeel van een software pakket. De <a href="http://78.20.29.170:5000/">website</a> is hier ook onderdeel van.



## References

* [SpanningLinearLayoutManager](https://gist.github.com/heinrichreimer/39f9d2f9023a184d96f8)




## Auteur
* **Lars Vandenberghe** - [LarsVandenberghe](https://github.com/LarsVandenberghe)