package it.uniroma3.torneidicalcio.controller;

import it.uniroma3.torneidicalcio.exception.PartitaDuplicataException;
import it.uniroma3.torneidicalcio.exception.SquadraDuplicataException;
import it.uniroma3.torneidicalcio.exception.TorneoDuplicataException;
import it.uniroma3.torneidicalcio.model.Giocatore;
import it.uniroma3.torneidicalcio.model.Partita;
import it.uniroma3.torneidicalcio.model.Squadra;
import it.uniroma3.torneidicalcio.model.Stato;
import it.uniroma3.torneidicalcio.model.Torneo;
import it.uniroma3.torneidicalcio.service.*;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Controller
@RequestMapping("/admin")
public class AdminController {

    private final TorneoService torneoService;
    private final SquadraService squadraService;
    private final GiocatoreService giocatoreService;
    private final PartitaService partitaService;
    private final ArbitroService arbitroService;
    private final ImageUploadService imageUploadService;

    public AdminController(TorneoService torneoService, SquadraService squadraService, GiocatoreService giocatoreService, PartitaService partitaService, ArbitroService arbitroService, ImageUploadService imageUploadService) {
        this.torneoService = torneoService;
        this.squadraService = squadraService;
        this.giocatoreService = giocatoreService;
        this.partitaService = partitaService;
        this.arbitroService = arbitroService;
        this.imageUploadService = imageUploadService;
    }

    // TORNEI
    @GetMapping("/tornei/new")
    public String createTorneoForm(Model model) {
        model.addAttribute("torneo", new Torneo());
        return "admin/formTornei";
    }

    @PostMapping("/tornei/new")
    public String createTorneo(@Valid @ModelAttribute("torneo") Torneo torneo, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "admin/formTornei";
        }
        try {
            this.torneoService.save(torneo);
            return "redirect:/tornei/";
        }
        catch (TorneoDuplicataException e) {
            bindingResult.reject("torneo.duplicato", null);
        }
        return "admin/formTornei";
    }

    @GetMapping("/tornei/{id}/edit")
    public String editTorneoForm(@PathVariable Long id, Model model) {
        Torneo torneo = this.torneoService.findByIdWithDetails(id);
        if (torneo == null) {
            return "redirect:/tornei/";
        }

        Set<Partita> partite = torneo.getPartite();
        Set<Squadra> squadre = torneo.getSquadre();

        List<Squadra> squadreAttive = new ArrayList<>();
        List<Squadra> squadreEliminate = new ArrayList<>();
        if (squadre != null) {
            for (Squadra s : squadre) {
                if (s.isEliminata()) {
                    squadreEliminate.add(s);
                } else {
                    squadreAttive.add(s);
                }
            }
        }

        List<Partita> partiteAttive = new ArrayList<>();
        List<Partita> partiteEliminate = new ArrayList<>();
        if (partite != null) {
            for (Partita p : partite) {
                if (p.isEliminata()) {
                    partiteEliminate.add(p);
                } else {
                    partiteAttive.add(p);
                }
            }
        }

        List<Squadra> tutteLeSquadre = this.squadraService.findAll();
        List<Squadra> squadreDisponibili = new ArrayList<>();
        if (tutteLeSquadre != null) {
            for (Squadra s : tutteLeSquadre) {
                //  se non è eliminata e non è già nel torneo
                if (!s.isEliminata() && (squadre == null || !squadre.contains(s))) {
                    squadreDisponibili.add(s);
                }
            }
        }

        model.addAttribute("torneo", torneo);
        model.addAttribute("partiteAttive", partiteAttive);
        model.addAttribute("partiteEliminate", partiteEliminate);
        model.addAttribute("squadreAttive", squadreAttive);
        model.addAttribute("squadreEliminate", squadreEliminate);
        model.addAttribute("squadreDisponibili", squadreDisponibili);

        return "admin/formTornei";
    }

    @PostMapping("/tornei/{id}/edit")
    public String editTorneo(@PathVariable Long id,  @Valid @ModelAttribute("torneo") Torneo torneo, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "admin/formTornei";
        }
        try {
            this.torneoService.update(id, torneo);
            return "redirect:/tornei/" + id;
        }
        catch (TorneoDuplicataException e) {
            bindingResult.reject("torneo.duplicato", null);
        }
        return "admin/formTornei";
    }

    @PostMapping("/tornei/{id}/squadre/add")
    public String addSquadraToTorneo(@PathVariable Long id, @RequestParam Long squadraId) {
        Torneo torneo = this.torneoService.findById(id);
        Squadra squadra = this.squadraService.findById(squadraId);
        if (torneo != null && squadra != null) {
            torneo.getSquadre().add(squadra);
            this.torneoService.aggiungiSquadra(id, squadra);
        }
        return "redirect:/admin/tornei/" + id + "/edit";
    }

    @PostMapping("/tornei/{tid}/squadre/{sid}/remove")
    public String removeSquadraFromTorneo(@PathVariable Long tid, @PathVariable Long sid) {
        Torneo torneo = this.torneoService.findById(tid);
        Squadra squadra = this.squadraService.findById(sid);
        if (torneo != null && squadra != null) {
            torneo.getSquadre().remove(squadra);  // rimuove solo il link nella tabella ManyToMany
            this.torneoService.rimuoviSquadra(tid, squadra);
        }
        return "redirect:/admin/tornei/" + tid + "/edit";
    }


    @PostMapping("/tornei/{id}/delete")
    public String deleteTorneo(@PathVariable Long id) {
        this.torneoService.deleteById(id);
        return "redirect:/tornei/";
    }


    // SQUADRE
    @GetMapping("/squadre/new")
    public String createSquadraForm(Model model) {
        model.addAttribute("squadra", new Squadra());
        return "admin/formSquadre";
    }

    @PostMapping("/squadre/new")
    public String createSquadra(@Valid @ModelAttribute("squadra") Squadra squadra,
                                BindingResult bindingResult,
                                @RequestParam(value = "stemma", required = false) MultipartFile stemma) {
        if (bindingResult.hasErrors()) {
            return "admin/formSquadre";
        }

        try {
            String stemmaUrl = this.imageUploadService.uploadStemma(stemma);
            if (stemmaUrl != null) {
                squadra.setStemmaUrl(stemmaUrl);
            }
            Squadra salvata = this.squadraService.iscriviSquadra(squadra);
            return "redirect:/squadre/" + salvata.getId();
        } catch (SquadraDuplicataException e) {
            bindingResult.reject("squadra.duplicata", new Object[]{e.getNomeSquadra()}, null);
            return "admin/formSquadre";
        }
    }

    @GetMapping("/squadre/{id}/edit")
    public String editSquadraForm(@PathVariable Long id, Model model) {
        Squadra squadra = this.squadraService.findById(id);
        if (squadra == null) return "redirect:/squadre/";
        model.addAttribute("squadra", squadra);
        return "admin/formSquadre";
    }

    @PostMapping("/squadre/{id}/edit")
    public String editSquadra(@PathVariable Long id, @Valid @ModelAttribute("squadra") Squadra squadra,
                              BindingResult bindingResult,
                              @RequestParam(value = "stemma", required = false) MultipartFile stemma) {
        if (bindingResult.hasErrors()) {
            return "admin/formSquadre";
        }
        String stemmaUrl = this.imageUploadService.uploadStemma(stemma);
        if (stemmaUrl != null) {
            squadra.setStemmaUrl(stemmaUrl);
        }
        this.squadraService.update(id, squadra);
        return "redirect:/squadre/" + id;
    }

    // la squadra viene marcata come eliminata, non cancellata dal DB
    @PostMapping("/squadre/{id}/delete")
    public String deleteSquadra(@PathVariable Long id) {
        Squadra squadra = this.squadraService.findById(id);
        if (squadra != null) {

            this.squadraService.deleteById(squadra.getId());
        }
        return "redirect:/squadre/";
    }

    @PostMapping("/squadre/{sid}/giocatori/{gid}/remove")
    public String removeGiocatoreFromSquadra(@PathVariable Long sid, @PathVariable Long gid) {
        Giocatore giocatore = this.giocatoreService.findById(gid);
        if (giocatore != null) {
            giocatore.setSquadra(null);  // toglie il giocatore dalla squadra
            this.giocatoreService.save(giocatore);
        }
        return "redirect:/admin/squadre/" + sid + "/edit";
    }



    // GIOCATORI
    @GetMapping("/giocatori/new")
    public String createGiocatoreForm(Model model) {
        model.addAttribute("giocatore", new Giocatore());
        model.addAttribute("squadre", this.squadraService.findAll());
        return "admin/formGiocatore";
    }

    @PostMapping("/giocatori/new")
    public String createGiocatore(@Valid @ModelAttribute("giocatore") Giocatore giocatore, BindingResult bindingResult, Model model) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("squadre", this.squadraService.findAll());
            return "admin/formGiocatore";
        }
        this.giocatoreService.save(giocatore);
        return "redirect:/squadre/";
    }

    @GetMapping("/giocatori/{id}/edit")
    public String editGiocatoreForm(@PathVariable Long id, Model model) {
        Giocatore giocatore = this.giocatoreService.findById(id);
        if (giocatore == null) return "redirect:/squadre/";
        model.addAttribute("giocatore", giocatore);
        model.addAttribute("squadre", this.squadraService.findAll());
        return "admin/formGiocatore";
    }

    @PostMapping("/giocatori/{id}/edit")
    public String editGiocatore(@PathVariable Long id, @Valid @ModelAttribute("giocatore") Giocatore giocatore, BindingResult bindingResult, Model model) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("squadre", this.squadraService.findAll());
            return "admin/formGiocatore";
        }
        this.giocatoreService.update(id, giocatore);
        return "redirect:/giocatori/" + id;
    }

    @PostMapping("/giocatori/{id}/delete")
    public String deleteGiocatore(@PathVariable Long id) {
        Giocatore giocatore = this.giocatoreService.findById(id);
        if (giocatore != null) {
            Long squadraId = giocatore.getSquadra() != null ? giocatore.getSquadra().getId() : null;
            this.giocatoreService.deleteById(id);
            if (squadraId != null) return "redirect:/squadre/" + squadraId;
        }
        return "redirect:/squadre/";
    }



    // PARTITE
    @GetMapping("/partite/new")
    public String createPartitaForm(@RequestParam(required = false) Long torneoId, Model model) {
        Partita partita = new Partita();
        if (torneoId != null) {
            Torneo torneo = this.torneoService.findById(torneoId);
            if (torneo != null) {
                partita.setTorneo(torneo);
            }
        }
        model.addAttribute("partita", partita);
        popolaFormPartita(model, torneoId);

        return "admin/formPartite";
    }

    @PostMapping("/partite/new")
    public String createPartita(@Valid @ModelAttribute("partita") Partita partita, BindingResult bindingResult, Model model) {
        if (bindingResult.hasErrors()) {
            Long torneoId = partita.getTorneo() != null ? partita.getTorneo().getId() : null;
            popolaFormPartita(model, torneoId);
            return "admin/formPartite";
        }
        try {
            partita.setStato(Stato.SCHEDULED);
            Partita salvata = this.partitaService.save(partita);
            return "redirect:/tornei/" + salvata.getTorneo().getId();
        } catch (PartitaDuplicataException e) {
            bindingResult.reject("partita.duplicata", null);
            Long torneoId = partita.getTorneo() != null ? partita.getTorneo().getId() : null;
            popolaFormPartita(model, torneoId);
            return "admin/formPartite";
        }
    }

    private void popolaFormPartita(Model model, Long torneoId) {
        model.addAttribute("tornei", this.torneoService.findAllOrdinatiPerAnno());
        model.addAttribute("torneoSelezionatoId", torneoId);
        model.addAttribute("arbitri", arbitroService.findAll());
        List<Squadra> squadreDelTorneo = torneoId != null
                ? this.torneoService.findSquadreByTorneoId(torneoId)
                : List.of();
        model.addAttribute("squadre", squadreDelTorneo);
    }

    @GetMapping("/partite/{id}/edit")
    public String editPartitaForm(@PathVariable Long id, @RequestParam(required = false) Long torneoId,  Model model) {
        Partita partita = this.partitaService.findById(id);
        if (partita == null) return "redirect:/tornei/";

        Long torneoSelezionatoId = torneoId != null
                ? torneoId
                : (partita.getTorneo() != null ? partita.getTorneo().getId() : null);

        model.addAttribute("partita", partita);
        popolaFormPartita(model, torneoSelezionatoId);
        return "admin/formPartite";
    }

    @PostMapping("/partite/{id}/edit")
    public String editPartita(@PathVariable Long id, @Valid @ModelAttribute("partita") Partita partita, BindingResult bindingResult, Model model) {
        if (bindingResult.hasErrors()) {
            Long torneoId = partita.getTorneo() != null ? partita.getTorneo().getId() : null;
            popolaFormPartita(model, torneoId);
            return "admin/formPartite";
        }
        try {
            this.partitaService.update(id, partita);
            return "redirect:/partite/" + id;
        } catch (PartitaDuplicataException e) {
            bindingResult.reject("partita.duplicata", null);
            Long torneoId = partita.getTorneo() != null ? partita.getTorneo().getId() : null;
            popolaFormPartita(model, torneoId);
            return "admin/formPartite";
        }
    }

    @PostMapping("/partite/{id}/delete")
    public String deletePartita(@PathVariable Long id) {
        Partita partita = this.partitaService.findById(id);
        if (partita == null) return "redirect:/tornei/";
        Long torneoId = partita.getTorneo() != null ? partita.getTorneo().getId() : null;
        partitaService.deleteById(partita.getId());
        return torneoId != null
                ? "redirect:/tornei/" + torneoId
                : "redirect:/tornei/";
    }


    // RISULTATI
    @GetMapping("/partite/{id}/risultato")
    public String risultatoForm(@PathVariable Long id, Model model) {
        Partita partita = this.partitaService.findById(id);
        if (partita == null) return "redirect:/tornei/";
        model.addAttribute("partita", partita);
        return "admin/risultato";
    }

    @PostMapping("/partite/{id}/risultato")
    public String inserisciRisultato(@PathVariable Long id, @RequestParam Integer goalsHome, @RequestParam Integer goalsAway) {
        this.partitaService.inserisciRisultato(id, goalsHome, goalsAway);
        return "redirect:/partite/" + id;
    }

    @PostMapping("/partite/{id}/risultato/edit")
    public String modificaRisultato(@PathVariable Long id, @RequestParam Integer goalsHome, @RequestParam Integer goalsAway) {
        this.partitaService.modificaRisultato(id, goalsHome, goalsAway);
        return "redirect:/partite/" + id;
    }
}