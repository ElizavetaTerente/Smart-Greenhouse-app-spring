package at.qe.skeleton.ui.controllers;

import java.io.IOException;
import java.util.Collection;
import java.util.stream.Collectors;
import java.util.ArrayList;

import at.qe.skeleton.api.storage.FileSystemStorageService;
import at.qe.skeleton.model.SensorStation;
import at.qe.skeleton.model.StationImage;
import at.qe.skeleton.repositories.StationImageRepository;
import at.qe.skeleton.services.StationImageService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.method.annotation.MvcUriComponentsBuilder;

// With help from guide https://spring.io/guides/gs/uploading-files/ to creating a server application that can receive HTTP multi-part file uploads.


import at.qe.skeleton.api.storage.StorageService;

@RestController
public class FileUploadController {

	private final StorageService storageService;

	public ArrayList<String> fileNames = new ArrayList<String>(); 

	@Autowired
	public FileUploadController(StorageService storageService) {
		this.storageService = storageService;
	}

	@Autowired
	StationImageService stationImageService;

	@Autowired
	StationImageRepository stationImageRepository;

	@Autowired
	FileSystemStorageService fileSystemStorageService;

	@GetMapping("/")
	public void listUploadedFiles(Model model) throws IOException {

		model.addAttribute("files", storageService.loadAll().map(
				path -> MvcUriComponentsBuilder.fromMethodName(FileUploadController.class,
						"serveFile", path.getFileName().toString()).build().toUri().toString())
				.collect(Collectors.toList()));
	}

	@GetMapping("/gallery/files/{filename:.+}")
	public ResponseEntity<Resource> serveFile(@PathVariable String filename) {

		Resource file = storageService.loadAsResource(filename);

		return ResponseEntity.ok().header(HttpHeaders.CONTENT_DISPOSITION,
			"attachment; filename=\"" + file.getFilename() + "\"").body(file);
	
	}

	@PostMapping("/uploadPic/{id}")
	public void handleFileUpload(@PathVariable Long id,@RequestParam("file") MultipartFile file) {

		storageService.store(file);
		fileNames.add( file.getOriginalFilename() );
		System.out.println("saved " +  file.getOriginalFilename() );
		stationImageService.uploadPic(id,file.getOriginalFilename());
		//return "redirect:/admin/concreteHouse.html?id=" + id;
	}

	@PostMapping("/besucher/uploadPic/{id}")
	public void handleFileUploadBesucher(@PathVariable Long id,@RequestParam("file") MultipartFile file) {

		storageService.store(file);
		fileNames.add( file.getOriginalFilename() );
		System.out.println("saved " +  file.getOriginalFilename() );
		stationImageService.uploadPic(id,file.getOriginalFilename());
		//return "redirect:/admin/concreteHouse.html?id=" + id;
	}

	@GetMapping("/loadAllPicsOfSensorStation/{id}")
	public Collection<StationImage> loadAllActivePicsOfSensorStation(@PathVariable Long id) {
		return stationImageService.loadAllActivePicsOfSensorStation(id);
	}

	@GetMapping("/besucher/loadAllPicsOfSensorStation/{id}")
	public Collection<StationImage> loadAllActivePicsOfSensorStationBesucher(@PathVariable Long id) {
		return stationImageService.loadAllActivePicsOfSensorStation(id);
	}

	@DeleteMapping ("/deletePic/{id}")
	public void deletePic(@PathVariable Long id) {
        StationImage stationImage = stationImageService.loadById(id);
		System.out.println("delete pic : " + stationImage.getPicturePath());

		stationImage.setEnabled(false);
		stationImageRepository.save(stationImage);
		String[] name = stationImage.getPicturePath().split("/");

		fileSystemStorageService.delete(name[name.length-1]);
	  }
	}



	/*
	@ExceptionHandler(StorageFileNotFoundException.class)
	public ResponseEntity<?> handleStorageFileNotFound(StorageFileNotFoundException exc) {
		return ResponseEntity.notFound().build();
	}
	 */

