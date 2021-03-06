package devs.nure.filesmanagerservice.controllers;

import devs.nure.filesmanagerservice.services.FilesService;
import devs.nure.formslibrary.*;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/v1/manager")
public class FilesController {

    private final FilesService filesService;
    public FilesController(FilesService filesService) {
        this.filesService = filesService;
    }

    @PostMapping(value = "/save", consumes = "multipart/form-data" )
    public FileInfo saveFile(@RequestParam("file") MultipartFile file,
                             @RequestParam("author") String author,
                             @RequestParam("parentID") String parentID){
        return filesService.manageFile(file, new CreateFile(parentID, author));
    }

    @GetMapping("/{fileID}")
    public ResponseEntity<Resource> uploadFile(@PathVariable String fileID){
        Resource resource = filesService.getFileRes(fileID);
        FileInfo info = filesService.getFileInfo(fileID);
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_TYPE, info.getContentType())
                .header(HttpHeaders.LAST_MODIFIED, info.getLastModification().toString())
                .body(resource);
    }

    @GetMapping("/{fileID}/download")
    public ResponseEntity<Resource> uploadFileAsAttachment(@PathVariable String fileID){
        Resource resource = filesService.getFileResDwnld(fileID);
        FileInfo info = filesService.getFileInfo(fileID);
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" +info.getName() + "\"")
                .header(HttpHeaders.CONTENT_TYPE, info.getContentType())
                .header(HttpHeaders.LAST_MODIFIED, info.getLastModification().toString())
                .body(resource);
    }

    @DeleteMapping("/remove")
    public void removeFile(@RequestParam("fileId") String fileId) {
        filesService.removeFile(fileId);
    }

}
